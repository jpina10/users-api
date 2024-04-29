package com.users.api.factory;

import com.users.api.dto.CreateUserDto;
import com.users.api.model.Address;
import com.users.api.model.User;
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
        user.setEmail("email");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setPhoneNumber("phoneNumber");
        user.setMainAddressId(getAddress().getId());

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

    public Address getAddress() {
        Address address = new Address();
        address.setId(1L);
        address.setStreet("street");
        address.setNumber("number");
        address.setCity("city");
        address.setCountry("country");
        address.setPostCode("postcode");

        return address;
    }

    public CreateUserDto getCreateUserDto() {
        CreateUserDto createUserDto = new CreateUserDto();
        createUserDto.setUsername("username");

        return createUserDto;
    }
}