package com.example.pasteservice.controller;

import com.example.pasteservice.controller.exception.UserNotFoundException;
import com.example.pasteservice.controller.payload.NewUserPayload;
import com.example.pasteservice.controller.payload.UpdateUserPayload;
import com.example.pasteservice.entity.User;
import com.example.pasteservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("user-api/")
public class UserController {

    private final UserService userService;

    @GetMapping("all")
    public List<User> findAllUsers() {
        return this.userService.findAll();
    }

    @GetMapping("find-by-username/{username}")
    public User findUserById(@PathVariable("username") String username) {
        Optional<User> findUser = this.userService.findByUsername(username);
        return findUser.orElseThrow(() -> new UserNotFoundException("User with username " + username + " not found.") );
    }

    @PostMapping("create")
    public ResponseEntity<?> createUser(@Valid @RequestBody NewUserPayload payload,
                                        UriComponentsBuilder uriBuilder) {
        User user = this.userService.save(payload.username(), payload.password());
        return ResponseEntity
                .created(uriBuilder
                        .replacePath("/user-api/find-by-username/{username}")
                        .build(Map.of("username", user.getUsername())))
                .body(user);
    }

    @GetMapping("find-by-id/{userId:\\d+}")
    public User findUserById(@PathVariable("userId") Long userId) {
        Optional<User> findUser = this.userService.findById(userId);
        return findUser.orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found.") );
    }

    @PatchMapping("update/{userId:\\d+}")
    public ResponseEntity<?> updateProfile(@PathVariable("userId") Long id,
                                           @Valid @RequestBody UpdateUserPayload payload) {
        this.userService.updateProfile(payload.username(), payload.password(), id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("delete/{userId:\\d+}")
    public ResponseEntity<Void> deleteUser(@PathVariable("userId") Long id) {
        this.userService.delete(id);
        return ResponseEntity.noContent().build();
    }



}
