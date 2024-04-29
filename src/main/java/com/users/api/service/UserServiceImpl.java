package com.users.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.users.api.dto.CreateUserDto;
import com.users.api.dto.UserCriteriaSpecification;
import com.users.api.dto.UserDto;
import com.users.api.dto.UserSearchCriteriaDto;
import com.users.api.exception.model.AccessException;
import com.users.api.exception.thirdparty.NameApiException;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.json.JsonPatch;
import javax.json.JsonStructure;
import javax.json.JsonValue;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

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
    public List<UserDto> findAllUsers(Pageable pageable) {
        var users = userRepository.findAll(pageable);

        return users.map(userMapper::toDto).toList();
    }

    @Override
    public UserDto findUserByUserName(String username) {
        log.info("Retrieving user with username: {}", username);
        return userRepository.findByUsername(username)
                .map(userMapper::toDto)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    @Override
    public List<UserDto> findUsersByCriteria(UserSearchCriteriaDto searchCriteria, Pageable pageable) {
        var specification = ifSearchingByFieldAddToSpecification(searchCriteria);

        Page<User> userPage = userRepository.findAll(specification, pageable);

        return userPage.map(userMapper::toDto).toList();
    }

    @Override
    @Transactional
    public UserDto createUser(CreateUserDto createUserDto) {
        existsUser(createUserDto.getUsername());

        log.info("saving user with username {}", createUserDto.getUsername());
        User user = userRepository.save(userMapper.toEntity(createUserDto));

        return userMapper.toDto(user);
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

    @Override
    @Transactional
    public void deleteUser(String username) {
        User user = this.getUser(username);

        log.debug("deleting user with username {}", user.getUsername());
        userRepository.delete(user);
    }

    @Override
    @Transactional
    public void updateUser(String username, JsonPatch jsonPatch) {
        User originalUser = this.getUser(username);
        log.debug("original user {}", originalUser);

        JsonStructure target = objectMapper.convertValue(originalUser, JsonStructure.class);
        JsonValue patched = jsonPatch.apply(target);

        var patchedUser = objectMapper.convertValue(patched, User.class);

        log.debug("saving patched user {}", patchedUser);
        userRepository.save(patchedUser);
    }

    @Override
    @Transactional
    public void enableUser(String username) {
        var user = this.getUser(username);

        user.setEnabled(true);
        userRepository.save(user);
    }

    private void setDefaultRole(User user) {
        user.addRole(Role.USER);
    }

    private void setAddressSection(Result userData, User user) {
        var address = addressMapper.toEntity(userData.getLocation());

        addressRepository.save(address);

        user.addAddress(address);
    }

    private User getUser(String username) {
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

            throw new NameApiException(errorMessage);
        }

        return response;
    }

    private boolean hasError(RandomUserApiResponse response) {
        return response.getError() != null;
    }

    private void existsUser(String username) {
        userRepository.findByUsername(username).ifPresent(user -> {
            throw new ResourceAlreadyExistsException(username);
        });
    }

    private Specification<User> ifSearchingByFieldAddToSpecification(UserSearchCriteriaDto userSearchCriteriaDto) {
        var fields = userSearchCriteriaDto.getClass().getDeclaredFields();

        Specification<User> spec = Specification.where(null);

        for (Field field : fields) {
            try {
                field.setAccessible(true);
                if (Objects.nonNull(field.get(userSearchCriteriaDto))) {
                    spec = spec.and(UserCriteriaSpecification.addField(field.getName(), field.get(userSearchCriteriaDto).toString()));
                }
            } catch (IllegalAccessException exception) {
                throw new AccessException(field.getName());
            }
        }

        return spec;
    }
}
