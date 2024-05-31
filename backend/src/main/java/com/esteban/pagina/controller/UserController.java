package com.esteban.pagina.controller;

import com.esteban.pagina.model.User;
import com.esteban.pagina.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;
    private final ErrorHandler errorHandler;

    public UserController(UserService userService, ErrorHandler errorHandler){
        this.userService = userService;
        this.errorHandler = errorHandler;
    }

    @GetMapping
    public ResponseEntity<Object> getAll(){
        List<User> users = userService.getAll();

        if(users == null){
            return errorHandler.createErrorResponse("Error interno del servidor mientras se extraían usuarios de la base de datos", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(users.isEmpty()){
            return errorHandler.createErrorResponse("No hay usuarios en la base de datos", HttpStatus.NOT_FOUND);
        }

        for(User user : users){
            user.setContrasena("");
        }

        return new ResponseEntity<>(new ArrayList<>(users), HttpStatus.OK);
    }

    @GetMapping("/{userIdOrName}")
    public ResponseEntity<Object> getProductById(@PathVariable("userIdOrName") String userIdOrName){
        Optional<User> optUser;

        if(userIdOrName.matches("\\d+")){
            Long userId = Long.valueOf(userIdOrName);

            if(userId < 1) {
                return errorHandler.createErrorResponse("Identificador de usuario inválido. Solo se permiten números enteros positivos", HttpStatus.BAD_REQUEST);
            }

            optUser = userService.getUserById(userId);
        } else {
            optUser = userService.getUserByName(userIdOrName);
        }

        if(optUser.isEmpty()){
            return errorHandler.createErrorResponse("No se encontró el usuario", HttpStatus.NOT_FOUND);
        }

        optUser.get().setContrasena("");

        return new ResponseEntity<>(optUser.get(), HttpStatus.OK);
    }



    @PostMapping("/create")
    public ResponseEntity<Object> saveUser(@RequestBody User user){
        if(user.getIdUsuario() != null){
            return errorHandler.createErrorResponse("El identificador del usuario se genera automaticamente", HttpStatus.NOT_ACCEPTABLE);
        }

        Optional<User> optUser = userService.getUserByName(user.getNombre());

        if(optUser.isPresent()){
            return errorHandler.createErrorResponse("Ya existe un usuario con ese nombre", HttpStatus.NOT_ACCEPTABLE);
        }

        String errorMessage = validateUser(user);

        if(!errorMessage.isEmpty()){
            return errorHandler.createErrorResponse(errorMessage, HttpStatus.BAD_REQUEST);
        }

        String hash_pw = BCrypt.hashpw(user.getContrasena(), BCrypt.gensalt());
        user.setContrasena(hash_pw);

        User createdUser = userService.saveOrUpdate(user);

        if(createdUser == null){
            return errorHandler.createErrorResponse("Error interno del servidor mientras se creaba el usuario", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        createdUser.setContrasena("");

        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<Object> updateUser(@RequestBody User user){
        final Long newUserId = user.getIdUsuario();

        if(newUserId == null){
            return errorHandler.createErrorResponse("Falta el identificador del usuario", HttpStatus.BAD_REQUEST);
        }

        Optional<User> optExistingUser = userService.getUserById(newUserId);

        if(optExistingUser.isEmpty()){
            return errorHandler.createErrorResponse("No existe un usuario con ese identificador", HttpStatus.NOT_FOUND);
        }

        if(user.getNombre() == null || user.getNombre().isEmpty()){
            user.setNombre(optExistingUser.get().getNombre());
        }
        if(user.getContrasena() == null || user.getContrasena().isEmpty()){
            user.setContrasena(optExistingUser.get().getContrasena());
        } else {
            String hash_pw = BCrypt.hashpw(user.getContrasena(), BCrypt.gensalt());
            user.setContrasena(hash_pw);
        }
        if(user.getTipo() == 0){
            user.setTipo(optExistingUser.get().getTipo());
        }

        if(user.equals(optExistingUser.get())){
            return errorHandler.createErrorResponse("El usuario no tiene cambios", HttpStatus.BAD_REQUEST);
        }

        if(!user.getNombre().equals(optExistingUser.get().getNombre())){
            Optional<User> optUser = userService.getUserByName(user.getNombre());

            if(optUser.isPresent()){
                return errorHandler.createErrorResponse("Ya existe un usuario con ese nombre", HttpStatus.NOT_ACCEPTABLE);
            }
        }

        String errorMessage = validateUser(user);

        if(!errorMessage.isEmpty()){
            return errorHandler.createErrorResponse(errorMessage, HttpStatus.BAD_REQUEST);
        }

        User updatedUser = userService.saveOrUpdate(user);

        if(updatedUser == null){
            return errorHandler.createErrorResponse("Error interno del servidor mientras se actualizaba el usuario", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        updatedUser.setContrasena("");

        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{userId:[0-9]+}")
    public ResponseEntity<Object> deleteUserById(@PathVariable("userId") Long userId){
        Optional<User> optUserToDel = userService.getUserById(userId);

        if(optUserToDel.isEmpty()){
            return errorHandler.createErrorResponse("No se encontró el usuario", HttpStatus.BAD_REQUEST);
        }

        String delResponse = userService.deleteUserById(userId);

        if(!delResponse.isEmpty()){
            return errorHandler.createErrorResponse(delResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(optUserToDel, HttpStatus.OK);
    }



    private String validateUser(User newUser){
        final String newUserName = newUser.getNombre();
        final String newUserContact = newUser.getContacto();
        final String newUserPass = newUser.getContrasena();
        final int newUserType = newUser.getTipo();

        if(newUserName.length() < 3 || newUserName.length() > 30){
            return "El nombre debe contener entre 3 y 30 caractéres";
        } if(newUserName.matches(".*\\d.*")){
            return "El nombre no debe contener números";
        } if(newUserPass.length() < 4 || newUserPass.length() > 254){
            return "La clave debe contener entre 4 y 255 caractéres";
        } if(newUserType < 1 || newUserType > 3){
            return "El tipo de usuario debe ser 1 (Mayorista), 2 (Encargado) o 3 (Jefe)";
        } if(newUserContact != null && !newUserContact.isEmpty()){
            if(newUserContact.length() < 3 || newUserContact.length() > 50){
                return "El contacto debe contener entre 3 y 50 caractéres";
            }
        }

        return "";
    }
}
