package com.modsen.bookstorageservice.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.modsen.bookstorageservice.domain.Role;
import com.modsen.bookstorageservice.web.dto.validation.OnCreate;
import com.modsen.bookstorageservice.web.dto.validation.OnUpdate;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    @NotNull(message = "Id must be not null", groups = OnUpdate.class)
    private Long id;

    @NotEmpty(message = "Username must be not null", groups = {OnUpdate.class, OnCreate.class})
    @Length(min = 4, max = 255, message = "Username length mus be smaller then 255")
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotEmpty(message = "Password must be not null", groups = {OnUpdate.class, OnCreate.class})
    @Length(min = 4, max = 255)
    private String password;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotEmpty(message = "Password confirmation must be not null", groups = OnCreate.class)
    private String passwordConfirmation;

    @NotEmpty(message = "Roles must be not empty", groups = {OnUpdate.class})
    private Set<Role> roles;
}