package com.users.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.users.api.dto.UserDto;
import com.users.api.exception.ThirdPartyException;
import com.users.api.exception.model.ResourceAlreadyExistsException;
import com.users.api.exception.model.UserNotFoundException;
import com.users.api.mapper.AddressMapper;
import com.users.api.mapper.RandomUserMapper;
import com.users.api.mapper.UserMapper;
import com.users.api.model.Role;
import com.users.api.model.User;
import com.users.api.nameapi.RandomUserApiResponse;
import com.users.api.nameapi.api.RandomUserApiClient;
import com.users.api.nameapi.model.Result;
import com.users.api.repository.AddressRepository;
import com.users.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
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
    private final AddressRepository addressRepository;

    private final UserMapper userMapper;
    private final RandomUserMapper randomUserMapper;
    private final AddressMapper addressMapper;
    private final ObjectMapper objectMapper;

    private final RandomUserApiClient randomUserApiClient;

    @Override
    public UserDto findUserByUserName(String username) {
        log.info("Retrieving user with username: {}", username);
        return userRepository.findByUsername(username)
                .map(userMapper::toDto)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    @Override
    @Transactional
    public String createRandomUser() {
        var response = callRandomUserApi();
        var userData = response.getResults().get(0);

        existsUser(userData.getLogin().getUsername());

        var user = randomUserMapper.toUser(userData);

        setAddressSection(userData, user);
        setDefaultRole(user);

        String username = user.getUsername();

        log.info("saving user with username {}", username);
        userRepository.save(user);

        return username;
    }

    private void setDefaultRole(User user) {
        user.addRole(Role.USER);
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

        log.debug("deleting user with username {}", user.getUsername());
        userRepository.delete(user);
    }

    @Override
    public List<UserDto> getAllUsers(Pageable pageable) {
        var users = userRepository.findAll(pageable);

        return users.stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public void updateUser(String username, JsonPatch jsonPatch) {
        User originalUser = this.findUser(username);
        log.debug("original user {}", originalUser);

        JsonStructure target = objectMapper.convertValue(originalUser, JsonStructure.class);
        JsonValue patched = jsonPatch.apply(target);

        var patchedUser = objectMapper.convertValue(patched, User.class);

        log.debug("saving patched user {}", patchedUser);
        userRepository.save(patchedUser);
    }

    private void setAddressSection(Result userData, User user) {
        var address = addressMapper.toEntity(userData.getLocation());

        addressRepository.save(address);

        user.addAddress(address);
    }

    private User findUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
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

    private void existsUser(String username) {
        userRepository.findByUsername(username).ifPresent(user1 -> {
            throw new ResourceAlreadyExistsException("The user with username " + username + " already exists.");
        });
    }
}
