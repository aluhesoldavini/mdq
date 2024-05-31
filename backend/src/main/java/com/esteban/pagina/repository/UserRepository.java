package com.esteban.pagina.repository;

import com.esteban.pagina.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByNombre(String nombre);
}
