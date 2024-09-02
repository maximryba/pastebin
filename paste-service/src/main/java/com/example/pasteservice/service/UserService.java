package com.example.pasteservice.service;

import com.example.pasteservice.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User save(String username, String password);

    Optional<User> findByUsername(String username);

    void delete(Long id);

    void updateProfile(String username, String password, Long id);

    List<User> findAll();

    Optional<User> findById(Long id);

}
