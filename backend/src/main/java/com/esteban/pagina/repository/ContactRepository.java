package com.esteban.pagina.repository;

import com.esteban.pagina.model.Contact;
import org.springframework.data.repository.CrudRepository;

public interface ContactRepository extends CrudRepository<Contact, Long> {
}
