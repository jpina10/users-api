package com.users.api.service;

import com.users.api.dto.PatchUserDto;
import com.users.api.dto.UserDto;
import com.users.api.exception.ThirdPartyException;
import com.users.api.exception.UserNotFoundException;
import com.users.api.mapper.PatchMapper;
import com.users.api.mapper.RandomUserMapper;
import com.users.api.mapper.UserMapper;
import com.users.api.model.User;
import com.users.api.model.UserDetails;
import com.users.api.nameapi.RandomUserApiResponse;
import com.users.api.nameapi.api.RandomUserApiClient;
import com.users.api.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.json.JsonPatch;
import javax.json.JsonStructure;
import javax.json.JsonValue;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RandomUserMapper randomUserMapper;
    private final PatchMapper patchMapper;
    private final RandomUserApiClient randomUserApiClient;
    private final ObjectMapper objectMapper;

    @Override
    public UserDto findUserByUserName(String username) {
        log.info("Retrieving user with username: {}", username);
        return userRepository.findByUsername(username)
                .map(user -> userMapper.toDto(user, user.getUserDetails()))
                .orElseThrow(() -> new UserNotFoundException("user with username " + username + " does not exist."));
    }

    @Override
    public String createRandomUser() {
        var response = callRandomUserApi();

        var userData = response.getResults().get(0);
        var user = randomUserMapper.toUser(userData);
        String username = user.getUsername();

        log.info("saving user with username {}", username);
        userRepository.save(user);

        return username;
    }

    @Override
    public void enableUser(String username) {
        var user = this.findUser(username);

        user.setEnabled(true);
        userRepository.save(user);
    }

    @Override
    public void deleteUser(String username) {
        User user = this.findUser(username);

        userRepository.delete(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(user -> {
                    UserDetails userDetails = user.getUserDetails();
                    return userMapper.toDto(user, userDetails);
                })
                .collect(Collectors.toList());
    }

    @Override
    public void updateUser(String username, JsonPatch jsonPatch) {
        User originalUser = this.findUser(username);
        log.debug("original user  {}", originalUser);

        JsonStructure target = objectMapper.convertValue(originalUser, JsonStructure.class);
        JsonValue patched = jsonPatch.apply(target);

        var patchedUser = objectMapper.convertValue(patched, User.class);

        userRepository.save(patchedUser);
    }


    private User findUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("user with username" + username + " does not exist."));
    }

    private RandomUserApiResponse callRandomUserApi() {
        log.info("calling random user API...");

        var response = randomUserApiClient.getUserData();
        var hasError = hasError(response);

        /*
         * the random user api doesn't send an exception when down but send a JSON with an error field, this is why a try-catch wasn't used
         * https://randomuser.me/documentation#errors
         * */
        if (hasError) {
            String errorMessage = response.getError().getErrorMessage();
            log.error(errorMessage);

            throw new ThirdPartyException(errorMessage);
        }

        return response;
    }

    private boolean hasError(RandomUserApiResponse response) {
        return response.getError() != null;
    }

}
