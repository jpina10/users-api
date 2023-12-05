/*
package com.users.api.service;

import com.users.api.dto.UserDto;
import com.users.api.exception.ThirdPartyException;
import com.users.api.exception.UserNotFoundException;
import com.users.api.factory.TestFactory;
import com.users.api.mapper.RandomUserMapper;
import com.users.api.mapper.UserMapper;
import com.users.api.model.User;
import com.users.api.nameapi.RandomUserApiResponse;
import com.users.api.nameapi.api.RandomUserApiClient;
import com.users.api.nameapi.model.Result;
import com.users.api.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
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

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void findByUserNameWithExistingUserName() {
        User user = testFactory.getUser();

        Optional<User> userOptional = Optional.of(user);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(userOptional);
        when(userMapper.toDto(user, user.getUserDetails())).thenReturn(Mockito.mock(UserDto.class));

        userService.findUserByUserName(USERNAME);

        verify(userRepository).findByUsername(USERNAME);
        verify(userMapper).toDto(user, user.getUserDetails());
    }

    @Test
    void findByUserNameWithNonExistingUserNameShouldThrowException() {
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findUserByUserName(USERNAME));

        verify(userRepository).findByUsername(USERNAME);
        verifyNoInteractions(userMapper);
    }

    @Test
    void createUserWithApiAvailable() {
        User user = testFactory.getUser();
        RandomUserApiResponse randomUserApiResponse = testFactory.getRandomUserApiResponse();
        Result result = randomUserApiResponse.getResults().get(0);

        when(randomUserApiClient.getUserData()).thenReturn(randomUserApiResponse);
        when(randomUserMapper.toUser(result)).thenReturn(user);

        userService.createRandomUser();

        verify(randomUserApiClient).getUserData();
        verify(randomUserMapper).toUser(result);
        verify(userRepository).save(user);
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
        when(userMapper.toDto(user, user.getUserDetails())).thenReturn(mock(UserDto.class));

        List<UserDto> allUsers = userService.getAllUsers();

        assertThat(allUsers.size()).isEqualTo(users.size());

        verify(userRepository).findAll();
        verify(userMapper).toDto(user, user.getUserDetails());
    }
}*/
