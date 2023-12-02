package com.users.api.mapper;

import com.users.api.dto.PatchUserDto;
import com.users.api.model.User;
import com.users.api.model.UserDetails;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.time.LocalDateTime;

@Mapper
public interface PatchMapper {
    @Mapping(target = "username", source = "patchUserDto.username")
    @Mapping(target = "password", source = "patchUserDto.password")
    @Mapping(target = "updatedOn", expression = "java(getNow())")
    @Mapping(target = "userDetails.id", source = "originalUser.userDetails.id")
    @Mapping(target = "userDetails.email", source = "patchUserDto.email")
    @Mapping(target = "userDetails.firstName", source = "patchUserDto.firstName")
    @Mapping(target = "userDetails.lastName", source = "patchUserDto.lastName")
    @Mapping(target = "userDetails.phoneNumber", source = "patchUserDto.phoneNumber")
    @Mapping(target = "userDetails.address.id", source = "originalUser.userDetails.address.id")
    @Mapping(target = "userDetails.address.street", source = "patchUserDto.street")
    @Mapping(target = "userDetails.address.number", source = "patchUserDto.number")
    @Mapping(target = "userDetails.address.city", source = "patchUserDto.city")
    @Mapping(target = "userDetails.address.country", source = "patchUserDto.country")
    @Mapping(target = "userDetails.address.postCode", source = "patchUserDto.postCode")
    User toEntity(PatchUserDto patchUserDto, User originalUser);

    @Mapping(target = "street", source = "userDetails.address.street")
    @Mapping(target = "number", source = "userDetails.address.number")
    @Mapping(target = "city", source = "userDetails.address.city")
    @Mapping(target = "country", source = "userDetails.address.country")
    @Mapping(target = "postCode", source = "userDetails.address.postCode")
    PatchUserDto toPatchUserDto(User user, UserDetails userDetails);

    default LocalDateTime getNow() {
        return LocalDateTime.now();
    }

/*    @AfterMapping
    default void setUserIntoUserDetails(@MappingTarget User user) {
        UserDetails userDetails = user.getUserDetails();
        userDetails.setUser(user);
    }*/


}
