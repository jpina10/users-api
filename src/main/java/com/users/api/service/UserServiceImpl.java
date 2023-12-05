package com.users.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.users.api.dto.UserDto;
import com.users.api.exception.ResourceNotFoundException;
import com.users.api.exception.ThirdPartyException;
import com.users.api.mapper.RandomUserMapper;
import com.users.api.mapper.UserMapper;
import com.users.api.model.Address;
import com.users.api.model.User;
import com.users.api.nameapi.RandomUserApiResponse;
import com.users.api.nameapi.api.RandomUserApiClient;
import com.users.api.nameapi.model.Location;
import com.users.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.json.JsonPatch;
import javax.json.JsonStructure;
import javax.json.JsonValue;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RandomUserMapper randomUserMapper;
    private final RandomUserApiClient randomUserApiClient;
    private final ObjectMapper objectMapper;
    private final AddressService addressService;

    @Override
    public UserDto findUserByUserName(String username) {
        log.info("Retrieving user with username: {}", username);
        return userRepository.findByUsername(username)
                .map(userMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("user with username " + username + " does not exist."));
    }

    @Override
    @Transactional
    public String createRandomUser() {
        var response = callRandomUserApi();

        var userData = response.getResults().get(0);
        var user = randomUserMapper.toUser(userData);
        String username = user.getUsername();

        log.info("saving user with username {}", username);
        userRepository.save(user);

        setUserAddress(user, userData.getLocation());
        return username;
    }

    @Override
    @Transactional
    public void enableUser(String username) {
        var user = this.findUser(username);

        user.setEnabled(true);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(String username) {
        User user = this.findUser(username);

        userRepository.delete(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public void updateUser(String username, JsonPatch jsonPatch) {
        User originalUser = this.findUser(username);
        log.debug("original user  {}", originalUser);

        JsonStructure target = objectMapper.convertValue(originalUser, JsonStructure.class);
        JsonValue patched = jsonPatch.apply(target);

        var patchedUser = objectMapper.convertValue(patched, User.class);

        userRepository.save(patchedUser);
    }

    private void setUserAddress(User user, Location location) {
        Address address = addressService.createAddress(user, location);
        user.setMainAddressId(address.getId());
    }

    private User findUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("user with username" + username + " does not exist."));
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
