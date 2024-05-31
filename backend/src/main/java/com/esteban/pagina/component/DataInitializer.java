package com.esteban.pagina.component;

import com.esteban.pagina.model.Contact;
import com.esteban.pagina.model.User;
import com.esteban.pagina.repository.ContactRepository;
import com.esteban.pagina.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    private final ContactRepository contactRepository;
    private final UserService userService;

    @Autowired
    public DataInitializer(ContactRepository contactRepository, UserService userService) {
        this.contactRepository = contactRepository;
        this.userService = userService;
    }

    @Override
    public void run(String... args) {
        if(contactRepository.findById(1L).isEmpty()){
            Contact defaultContact = new Contact(1L,
                    "+1234567890123",
                    "Esta es una descripcion de ejemplo Esta es una descripcion de ejemplo",
                    "ejemplo@email.com",
                    "Ejemplo 1234",
                    "Texto de ejemplo;https://ejemplo.com/1",
                    "Texto de ejemplo;https://ejemplo.com/1",
                    "Texto de ejemplo;https://ejemplo.com/1");

            contactRepository.save(defaultContact);
        }
        if(userService.getUserById(1L).isEmpty()){
            User defaultUser = new User(1L,
                    "Esteban",
                    "virtualmdq5838@gmail.com",
                    "Esteban123",
                    3);

            userService.saveOrUpdate(defaultUser);
        }
    }
}
