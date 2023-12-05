package com.users.api.mapper;

import com.users.api.model.User;
import com.users.api.nameapi.model.Result;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;

@Mapper
public interface RandomUserMapper {

    @Mapping(target = "username", source = "result.login.username")
    @Mapping(target = "password", source = "result.login.password")
    @Mapping(target = "createdOn", expression = "java(getNow())")
    @Mapping(target = "updatedOn", expression = "java(getNow())")
    @Mapping(target = "email", source = "result.email")
    @Mapping(target = "firstName", source = "result.name.firstName")
    @Mapping(target = "lastName", source = "result.name.lastName")
    @Mapping(target = "phoneNumber", source = "result.phoneNumber")
    User toUser(Result result);

    default LocalDateTime getNow() {
        return LocalDateTime.now();
    }

}
