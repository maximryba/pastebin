package com.example.pasteservice.controller;

import com.example.pasteservice.controller.exception.UserNotFoundException;
import com.example.pasteservice.controller.payload.NewUserPayload;
import com.example.pasteservice.controller.payload.UpdateUserPayload;
import com.example.pasteservice.entity.User;
import com.example.pasteservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("user-api/")
public class UserController {

    private final UserService userService;

    private final MessageSource messageSource;


    @GetMapping("all")
    public List<User> findAllUsers() {
        return this.userService.findAll();
    }

    @GetMapping("find-by-username/{username}")
    public User findUserByUsername(@PathVariable("username") String username) {
        Optional<User> findUser = this.userService.findByUsername(username);
        return findUser.orElseThrow(() -> new NoSuchElementException("pastebin.errors.user.not_found") );
    }

    @PostMapping("create")
    public ResponseEntity<?> createUser(@Valid @RequestBody NewUserPayload payload,
                                        BindingResult bindingResult,
                                        UriComponentsBuilder uriBuilder) throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            User user = this.userService.save(payload.username(), payload.password());
            return ResponseEntity
                    .created(uriBuilder
                            .replacePath("/user-api/find-by-username/{username}")
                            .build(Map.of("username", user.getUsername())))
                    .body(user);
        }
    }

    @GetMapping("find-by-id/{userId}")
    public User findUserById(@PathVariable("userId") Long userId) {
        Optional<User> findUser = this.userService.findById(userId);
        return findUser.orElseThrow(() -> new NoSuchElementException("pastebin.errors.user.not_found") );
    }

    @PatchMapping("update/{userId}")
    public ResponseEntity<?> updateProfile(@PathVariable("userId") Long id,
                                           @Valid @RequestBody UpdateUserPayload payload,
                                           BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            this.userService.updateProfile(payload.username(), payload.password(), id);
            return ResponseEntity.noContent().build();
        }
    }

    @DeleteMapping("delete/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable("userId") Long id) {
        this.userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ProblemDetail> handleNoSuchElementException(NoSuchElementException exception,
                                                                      Locale locale) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,
                        this.messageSource.getMessage(exception.getMessage(), new Object[0],
                                exception.getMessage(), locale)));
    }



}
