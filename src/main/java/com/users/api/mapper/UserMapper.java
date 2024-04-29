package com.users.api.mapper;

import com.users.api.dto.CreateUserDto;
import com.users.api.dto.UserDto;
import com.users.api.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;

@Mapper(uses = AddressMapper.class)
public interface UserMapper {

    UserDto toDto(User user);

    @Mapping(target = "createdOn", expression = "java(getNow())")
    @Mapping(target = "updatedOn", expression = "java(getNow())")
    User toEntity(CreateUserDto createUserDto);

    default LocalDateTime getNow() {
        return LocalDateTime.now();
    }

}