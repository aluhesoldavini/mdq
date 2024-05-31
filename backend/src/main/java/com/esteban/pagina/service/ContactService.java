package com.esteban.pagina.service;

import com.esteban.pagina.model.Contact;
import com.esteban.pagina.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactService {

    private final ContactRepository contactRepository;

    @Autowired
    public ContactService(ContactRepository contactRepository){
        this.contactRepository = contactRepository;
    }


    public Contact getContact() {
        return contactRepository.findById(1L).orElse(null);
    }

    public Contact updateContact(Contact contact) {
        try {
            contact.setIdContacto(1L);
            contactRepository.save(contact);
            return contact;
        } catch (Exception e) {
            return null;
        }
    }
}
