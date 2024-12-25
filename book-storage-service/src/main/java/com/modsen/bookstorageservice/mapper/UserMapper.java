package com.modsen.bookstorageservice.mapper;

import com.modsen.bookstorageservice.domain.User;
import com.modsen.bookstorageservice.dto.UserDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);

    User toEntity(UserDto dto);

    List<UserDto> toDto(List<User> users);
}