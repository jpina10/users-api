package com.users.api.factory;

import com.users.api.model.Address;
import com.users.api.model.User;
import com.users.api.model.UserDetails;
import com.users.api.nameapi.RandomUserApiResponse;
import com.users.api.nameapi.model.Error;
import com.users.api.nameapi.model.*;

import java.time.LocalDateTime;
import java.util.List;

public class TestFactory {

    public User getUser() {
        User user = new User();

        user.setId(1L);
        user.setUsername("username");
        user.setPassword("password");
        user.setCreatedOn(LocalDateTime.now());
        user.setUpdatedOn(LocalDateTime.now());
        user.setEnabled(false);
        user.setUserDetails(getUserDetails());

        return user;
    }

    public RandomUserApiResponse getRandomUserApiResponse() {
        return RandomUserApiResponse.builder()
                .error(null)
                .results(List.of(Result.builder()
                                .gender("gender")
                                .name(getName())
                                .location(getLocation())
                                .email("email")
                                .login(getLogin())
                        .build()))
                .build();
    }

    public RandomUserApiResponse getRandomUserApiResponseWithError() {
        return RandomUserApiResponse.builder()
                .error(Error.builder()
                        .errorMessage("Uh oh, something has gone wrong. Please tweet us @randomapi about the issue. Thank you.")
                        .build())
                .build();
    }

    private static Login getLogin() {
        return Login.builder()
                .username("username")
                .password("password")
                .build();
    }

    private static Location getLocation() {
        return Location.builder()
                .street(getStreet())
                .city("city")
                .country("country")
                .postCode("postcode")
                .build();
    }

    private static Street getStreet() {
        return Street.builder()
                .name("name")
                .number("number")
                .build();
    }

    private static Name getName() {
        return Name.builder()
                .firstName("firstName")
                .lastName("lastName")
                .build();
    }

    private UserDetails getUserDetails() {
        return UserDetails.builder()
                .id(1L)
                .email("email")
                .firstName("firstName")
                .lastName("lastName")
                .phoneNumber("phoneNumber")
                .address(getAddress())
                .build();
    }

    private Address getAddress() {
        return Address.builder()
                .id(1L)
                .street("street")
                .number("number")
                .city("city")
                .country("country")
                .postCode("postcode")
                .build();
    }
}