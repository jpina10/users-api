package com.users.api.mapper;

import com.users.api.model.User;
import com.users.api.model.UserDetails;
import com.users.api.nameapi.model.Result;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.time.LocalDateTime;

@Mapper
public interface RandomUserMapper {

    @Mapping(target = "username", source = "result.login.username")
    @Mapping(target = "password", source = "result.login.password")
    @Mapping(target = "createdOn", expression = "java(getNow())")
    @Mapping(target = "updatedOn", expression = "java(getNow())")
    @Mapping(target = "userDetails.email", source = "result.email")
    @Mapping(target = "userDetails.firstName", source = "result.name.firstName")
    @Mapping(target = "userDetails.lastName", source = "result.name.lastName")
    @Mapping(target = "userDetails.phoneNumber", source = "result.phoneNumber")
    @Mapping(target = "userDetails.address.city", source = "result.location.city")
    @Mapping(target = "userDetails.address.country", source = "result.location.country")
    @Mapping(target = "userDetails.address.postCode", source = "result.location.postCode")
    @Mapping(target = "userDetails.address.street", source = "result.location.street.name")
    @Mapping(target = "userDetails.address.number", source = "result.location.street.number")
    User toUser(Result result);

    default LocalDateTime getNow() {
        return LocalDateTime.now();
    }

    @AfterMapping
    default void setUserIntoUserDetails(@MappingTarget User user) {
        UserDetails userDetails = user.getUserDetails();
        userDetails.setUser(user);
    }
}
