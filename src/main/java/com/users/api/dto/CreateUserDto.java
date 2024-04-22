package com.users.api.dto;

import com.users.api.util.ValidationMessages;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateUserDto {

    @NotNull(message = ValidationMessages.CANNOT_BE_NULL)
    @NotEmpty(message = ValidationMessages.CANNOT_BE_EMPTY)
    private String username;

    @NotNull(message = ValidationMessages.CANNOT_BE_NULL)
    private String password;
    private String firstName;
    private String lastName;

    @NotNull(message = ValidationMessages.CANNOT_BE_NULL)
    private String email;
    private String phoneNumber;
}
