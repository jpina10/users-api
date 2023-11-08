package com.users.api.mapper;

import com.users.api.dto.UserDto;
import com.users.api.model.User;
import com.users.api.model.UserDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UserMapper {

    @Mapping(target = "location.street", source = "userDetails.address.street")
    UserDto toDto(User user, UserDetails userDetails);
}