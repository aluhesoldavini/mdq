package com.esteban.pagina.controller;

import com.esteban.pagina.model.Contact;
import com.esteban.pagina.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/contact")
public class ContactController {

    private final ContactService contactService;
    private final ErrorHandler errorHandler;

    @Autowired
    public ContactController(ContactService contactService, ErrorHandler errorHandler){
        this.contactService = contactService;
        this.errorHandler = errorHandler;
    }

    @GetMapping
    public ResponseEntity<Object> getContact(){
        Contact contact = contactService.getContact();

        if(contact == null){
            return errorHandler.createErrorResponse("Error interno del servidor mientras se obtenía la información de contacto", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(contact, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<Object> updateContact(@RequestBody Contact contact){
        Contact existingContact = contactService.getContact();

        if(contact.equals(existingContact)){
            return errorHandler.createErrorResponse("La información de contacto no tiene cambios", HttpStatus.BAD_REQUEST);
        }

        String errorMessage = validateContact(contact);

        if(!errorMessage.isEmpty()){
            return errorHandler.createErrorResponse(errorMessage, HttpStatus.BAD_REQUEST);
        }

        Contact updatedContact = contactService.updateContact(contact);

        if(updatedContact == null){
            return errorHandler.createErrorResponse("Error interno del servidor mientras se actualizaba la información de contacto", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(updatedContact, HttpStatus.OK);
    }



    private String validateContact(Contact newContact){
        final String newContCelu = newContact.getNumCelu();
        final String newContInfo = newContact.getInfo();
        final String newContEmail = newContact.getEmail();
        final String newContDir = newContact.getDireccion();
        final String newContLink1 = newContact.getLink1();
        final String newContLink2 = newContact.getLink2();
        final String newContLink3 = newContact.getLink3();

        if(newContCelu.length() < 6 || newContCelu.length() > 30){
            return "El número debe contener entre 6 y 30 caractéres";
        } if(newContCelu.matches("\\d")){
            return "El número de contacto no puede contener letras";
        } if (newContEmail.length() < 6 || newContEmail.length() > 50){
            return "El Email debe contener entre 6 y 50 caractéres";
        } if(newContDir.length() < 5 || newContDir.length() > 30){
            return "La dirección debe contener entre 5 y 30 caractéres";
        } if (newContInfo.length() < 30 || newContInfo.length() > 200){
            return "La descripción debe contener entre 10 y 200 caractéres";
        } if(newContLink1 != null && !newContLink1.isEmpty()){
            if(newContLink1.length() < 10 || newContLink1.length() > 120){
                return "Los enlaces deben contener entre 10 y 120 caractéres";
            } if(!newContLink1.contains(";")){
                return "Debe separar el texto del enlace con ';'";
            } if(!newContLink1.contains("http://") && !newContLink1.contains("https://")){
                return "Debe contener un enlace";
            }
        } if(newContLink2 != null && !newContLink2.isEmpty()){
            if(newContLink2.length() < 10 || newContLink2.length() > 120){
                return "Los enlace deben contener entre 10 y 120 caractéres";
            } if(!newContLink2.contains(";")){
                return "Debe separar el texto del enlace con ';'";
            } if(!newContLink2.contains("http://") && !newContLink2.contains("https://")){
                return"Debe contener un enlace";
            }
        } if(newContLink3 != null && !newContLink3.isEmpty()){
            if(newContLink3.length() < 10 || newContLink3.length() > 120){
                return "Los enlaces deben contener entre 10 y 120 caractéres";
            } if(!newContLink3.contains(";")){
                return "Debe separar el texto del enlace con ';'";
            } if(!newContLink3.contains("http://") && !newContLink3.contains("https://")){
                return "Debe contener un enlace";
            }
        }

        return "";
    }
}