package com.modsen.booktrackerservice.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "id",example = "2")
    private long id;

    @Schema(description = "username",example = "John")
    private String username;


    @Schema(description = "password",example = "password")
    private String password;

    @Transient
    @Schema(description ="passwordConfirmation don't save ind DB and must match password")
    private String passwordConfirmation;

    @Schema(description = "roles",example = "[ROLE_USER]")
    @ElementCollection(targetClass = Role.class)
    @CollectionTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING) // указываем, что роли будут храниться как строки
    @Column(name = "roles")
    private Set<Role> roles;


}
