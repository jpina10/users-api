package com.users.api.service;

import com.users.api.dto.CreateUserDto;
import com.users.api.dto.UserDto;
import com.users.api.dto.UserSearchCriteriaDto;
import org.springframework.data.domain.Pageable;

import javax.json.JsonPatch;
import java.util.List;

public interface UserService {
    UserDto findUserByUserName(String username);

    String createRandomUser();

    UserDto createUser(CreateUserDto createUserDto);

    void enableUser(String username);

    void deleteUser(String username);

    List<UserDto> findAllUsers(Pageable pageable);

    void updateUser(String username, JsonPatch jsonPatch);

    List<UserDto> findUsersByCriteria(UserSearchCriteriaDto searchCriteria, Pageable pageable);

    void addAddress(String username, String addressId);
}
