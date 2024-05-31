package com.esteban.pagina.service;

import com.esteban.pagina.model.User;
import com.esteban.pagina.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }


    public List<User> getAll() {
        try {
            List<User> users = new ArrayList<>();
            userRepository.findAll().forEach(users::add);
            return users;
        } catch (Exception e) {
            return null;
        }
    }

    public Optional<User> getUserById(Long userId) {
        try {
            return userRepository.findById(userId);
        } catch (Exception e){
            return Optional.empty();
        }
    }

    public Optional<User> getUserByName(String name) {
        try {
            return userRepository.findByNombre(name);
        } catch (Exception e){
            return Optional.empty();
        }
    }

    public User saveOrUpdate(User user) {
        try {
            userRepository.save(user);
            return user;
        } catch (Exception e) {
            return null;
        }
    }

    public String deleteUserById(Long userId) {
        try {
            userRepository.deleteById(userId);
            return "";
        } catch (Exception e) {
            return "Error interno del servidor mientras se eliminaba el usuario";
        }
    }
}
