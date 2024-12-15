package com.modsen.bookstorageservice.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Entity
@Table(name = "users_view")
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "id", example = "2")
    private Long id;

    @Schema(description = "username", example = "John")
    private String username;

    @Schema(description = "Password",example = "password")
    @Column(name = "password")
    private String password;

    @Transient
    @Schema(description ="passwordConfirmation don't save ind DB and must match password")
    private String passwordConfirmation;

    @Schema(description = "roles",example = "[ROLE_USER]")
    @ElementCollection(targetClass = Role.class)
    @CollectionTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "roles")
    private Set<Role> roles;

    @Schema(description = "Is deleted",example = "true")
    private Boolean deleted;

}
