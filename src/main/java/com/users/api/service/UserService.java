package com.users.api.service;

import com.users.api.dto.UserDto;
import org.springframework.data.domain.Pageable;

import javax.json.JsonPatch;
import java.util.List;

public interface UserService {
    UserDto findUserByUserName(String username);

    String createRandomUser();

    void enableUser(String username);

    void deleteUser(String username);

    List<UserDto> getAllUsers(Pageable pageable);

    void updateUser(String username, JsonPatch jsonPatch);
}
