package com.users.api.mapper;

import com.users.api.dto.UserDto;
import com.users.api.model.User;
import org.mapstruct.Mapper;

@Mapper(uses = AddressMapper.class)
public interface UserMapper {

    UserDto toDto(User user);
}