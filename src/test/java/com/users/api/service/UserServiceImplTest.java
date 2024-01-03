package com.users.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.users.api.dto.UserDto;
import com.users.api.exception.ResourceAlreadyExistsException;
import com.users.api.exception.ResourceNotFoundException;
import com.users.api.exception.ThirdPartyException;
import com.users.api.factory.TestFactory;
import com.users.api.mapper.RandomUserMapper;
import com.users.api.mapper.UserMapper;
import com.users.api.model.User;
import com.users.api.nameapi.RandomUserApiResponse;
import com.users.api.nameapi.api.RandomUserApiClient;
import com.users.api.nameapi.model.Location;
import com.users.api.nameapi.model.Result;
import com.users.api.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.json.Json;
import javax.json.JsonPatch;
import javax.json.JsonStructure;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith({SpringExtension.class})
class UserServiceImplTest {
    private static final String USERNAME = "username";

    public final TestFactory testFactory = new TestFactory();

    @Captor
    ArgumentCaptor<User> userArgumentCaptor;

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private RandomUserMapper randomUserMapper;
    @Mock
    private RandomUserApiClient randomUserApiClient;
    @Mock
    private AddressService addressService;
    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void findByUserNameWithExistingUserName() {
        User user = testFactory.getUser();

        Optional<User> userOptional = Optional.of(user);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(userOptional);
        when(userMapper.toDto(user)).thenReturn(mock(UserDto.class));

        userService.findUserByUserName(USERNAME);

        verify(userRepository).findByUsername(USERNAME);
    }

    @Test
    void findByUserNameWithNonExistingUserNameShouldThrowException() {
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.findUserByUserName(USERNAME));

        verify(userRepository).findByUsername(USERNAME);
        verifyNoInteractions(userMapper);
    }

    @Test
    void createUserWithApiAvailableAndNotExistingUsername() {
        User user = testFactory.getUser();
        RandomUserApiResponse randomUserApiResponse = testFactory.getRandomUserApiResponse();
        Result result = randomUserApiResponse.getResults().get(0);

        when(randomUserApiClient.getUserData()).thenReturn(randomUserApiResponse);
        when(randomUserMapper.toUser(result)).thenReturn(user);

        Location location = result.getLocation();
        when(addressService.createAddress(user, location)).thenReturn(user.getAddresses().get(0));

        userService.createRandomUser();

        verify(randomUserApiClient).getUserData();
        verify(randomUserMapper).toUser(result);
        verify(userRepository).save(user);
        verify(addressService).createAddress(user, location);
    }

    @Test
    void createUserWithApiAvailableAndExistingUsernameShouldThrowException() {
        User user = testFactory.getUser();
        RandomUserApiResponse randomUserApiResponse = testFactory.getRandomUserApiResponse();
        Result result = randomUserApiResponse.getResults().get(0);

        when(randomUserApiClient.getUserData()).thenReturn(randomUserApiResponse);
        when(randomUserMapper.toUser(result)).thenReturn(user);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        assertThrows(ResourceAlreadyExistsException.class, () -> userService.createRandomUser());

        verify(randomUserApiClient).getUserData();
        verify(randomUserMapper).toUser(result);
    }

    @Test
    void updateUser() {
        User originalUser = testFactory.getUser();

        JsonPatch patch = Json.createPatchBuilder()
                .replace("/firstName", "updated")
                .build();

        UserDto patchedUser = new UserDto();

        JsonPatch jsonPatch = mock(JsonPatch.class);
        JsonStructure jsonStructure = mock(JsonStructure.class);
        JsonStructure patchResult = mock(JsonStructure.class);

        when(jsonPatch.toJsonArray()).thenReturn(patch.toJsonArray());
        when(objectMapper.convertValue(originalUser, JsonStructure.class)).thenReturn(jsonStructure);
        when(jsonPatch.apply(jsonStructure)).thenReturn(patchResult);
        when(objectMapper.convertValue(patchResult, UserDto.class)).thenReturn(patchedUser);

        String username = originalUser.getUsername();
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(originalUser));

        userService.updateUser(username, jsonPatch);

        verify(userRepository).findByUsername(username);
    }

    @Test
    void createUserWithApiUnavailable() {
        when(randomUserApiClient.getUserData()).thenReturn(testFactory.getRandomUserApiResponseWithError());

        assertThrows(ThirdPartyException.class, () -> userService.createRandomUser());

        verify(randomUserApiClient).getUserData();
        verifyNoInteractions(randomUserMapper);
        verifyNoInteractions(userRepository);
    }

    @Test
    void enableExistingUser() {
        User user = testFactory.getUser();
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));

        userService.enableUser(user.getUsername());

        verify(userRepository).save(userArgumentCaptor.capture());
        User userCaptured = userArgumentCaptor.getValue();

        assertTrue(userCaptured.isEnabled());

        verify(userRepository).findByUsername(user.getUsername());
        verify(userRepository).save(user);
    }

    @Test
    void deleteUser() {
        User user = testFactory.getUser();
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));

        userService.deleteUser(user.getUsername());

        verify(userRepository).delete(user);
    }

    @Test
    void getAllUsers() {
        User user = testFactory.getUser();
        List<User> users = List.of(user);

        when(userRepository.findAll()).thenReturn(users);

        List<UserDto> allUsers = userService.getAllUsers();

        assertThat(allUsers).hasSameSizeAs(users);

        verify(userRepository).findAll();
    }
}
