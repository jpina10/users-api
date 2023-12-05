package com.users.api.service;

import com.users.api.dto.UserDto;

import javax.json.JsonPatch;
import java.util.List;

public interface UserService {
    UserDto findUserByUserName(String username);

    String createRandomUser();

    void enableUser(String username);

    void deleteUser(String username);

    List<UserDto> getAllUsers();

    void updateUser(String username, JsonPatch jsonPatch);
}
