package com.users.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.users.api.dto.UserDto;
import com.users.api.exception.model.ResourceAlreadyExistsException;
import com.users.api.exception.model.ResourceNotFoundException;
import com.users.api.exception.ThirdPartyException;
import com.users.api.factory.TestFactory;
import com.users.api.mapper.AddressMapper;
import com.users.api.mapper.RandomUserMapper;
import com.users.api.mapper.UserMapper;
import com.users.api.model.Address;
import com.users.api.model.User;
import com.users.api.nameapi.RandomUserApiResponse;
import com.users.api.nameapi.api.RandomUserApiClient;
import com.users.api.nameapi.model.Location;
import com.users.api.nameapi.model.Result;
import com.users.api.repository.AddressRepository;
import com.users.api.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private AddressMapper addressMapper;
    @Mock
    private AddressRepository addressRepository;
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
        Address address = testFactory.getAddress();

        RandomUserApiResponse randomUserApiResponse = testFactory.getRandomUserApiResponse();
        Result result = randomUserApiResponse.getResults().get(0);

        when(randomUserApiClient.getUserData()).thenReturn(randomUserApiResponse);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        when(randomUserMapper.toUser(result)).thenReturn(user);

        Location location = result.getLocation();
        when(addressMapper.toEntity(location)).thenReturn(address);
        when(addressRepository.save(address)).thenReturn(address);

        userService.createRandomUser();

        verify(randomUserApiClient).getUserData();
        verify(userRepository).findByUsername(user.getUsername());
        verify(randomUserMapper).toUser(result);
        verify(addressMapper).toEntity(location);
        verify(addressRepository).save(address);
        verify(userRepository).save(user);
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
        verifyNoInteractions(addressMapper);
        verifyNoInteractions(addressRepository);
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
        Pageable pageable = PageRequest.of(0, 20);
        PageImpl<User> userPage = new PageImpl<>(users, pageable, users.size());

        when(userRepository.findAll(pageable)).thenReturn(userPage);

        List<UserDto> allUsers = userService.getAllUsers(pageable);

        assertThat(allUsers).hasSameSizeAs(users);

        verify(userRepository).findAll(pageable);
    }
}
