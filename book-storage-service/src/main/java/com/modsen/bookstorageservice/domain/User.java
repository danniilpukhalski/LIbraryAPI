package com.modsen.bookstorageservice.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@Entity
@Table(name = "users_view")
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = false)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "id", example = "2")
    Long id;

    @Schema(description = "username", example = "John")
    String username;

    @Schema(description = "Password", example = "password")
    @Column(name = "password")
    String password;

    @Transient
    @Schema(description = "passwordConfirmation don't save ind DB and must match password")
    String passwordConfirmation;

    @Schema(description = "roles", example = "[ROLE_USER]")
    @ElementCollection(targetClass = Role.class)
    @CollectionTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "roles")
    Set<Role> roles;

}
