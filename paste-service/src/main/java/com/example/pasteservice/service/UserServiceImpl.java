package com.example.pasteservice.service;

import com.example.pasteservice.entity.Role;
import com.example.pasteservice.entity.User;
import com.example.pasteservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User save(String username, String password) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .role(Role.CLIENT)
                .build();

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username is already taken.");
        }
        return this.userRepository.save(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }

    @Override
    @Transactional
    @PreAuthorize("@userSecurityService.isThisUserOrAdmin(#id, authentication)")
    public void delete(Long id) {
        this.userRepository.delete(id);
    }

    @Override
    @Transactional
    @PreAuthorize("@userSecurityService.isThisUserOrAdmin(#id, authentication)")
    public void updateProfile(String username, String password, Long id) {
        this.userRepository.findById(id)
                .ifPresentOrElse(user -> {
                    user.setUsername(username);
                    user.setPassword(password);
                }, () -> {
                    throw new NoSuchElementException();
                });
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
