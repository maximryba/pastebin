package com.example.pasteservice.service;

import com.example.pasteservice.entity.Paste;
import com.example.pasteservice.entity.User;
import com.example.pasteservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User save(String username, String password) {
        User user = User.builder()
                .username(username)
                .password(password)
                .build();
        return this.userRepository.save(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }

    @Override
    public void delete(Long id) {
        this.userRepository.delete(id);
    }

    @Override
    public void updateProfile(String username, String password, Long id) {
        Optional<User> user = this.userRepository.findById(id);
       if (user.isPresent()) {
           User currentUser = user.get();
           currentUser.setPassword(password);
           currentUser.setUsername(username);
           this.userRepository.save(currentUser);
       }

    }

    @Override
    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        return this.userRepository.findById(id);
    }


}
