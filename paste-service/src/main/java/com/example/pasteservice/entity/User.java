package com.example.pasteservice.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true)
    @NotNull
    @Size(min = 4, max = 50)
    private String username;

    @Column(name = "password")
    @NotNull
    @Size(min = 4, max = 100)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    @NotNull
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Paste> pasteList;


}
