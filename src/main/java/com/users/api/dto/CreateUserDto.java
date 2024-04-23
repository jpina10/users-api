package com.users.api.dto;

import com.users.api.util.ValidationMessages;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = ValidationMessages.CANNOT_BE_BLANK)
    private String username;

    private String password;

    private String firstName;
    private String lastName;

    @NotBlank(message = ValidationMessages.CANNOT_BE_NULL_OR_EMPTY)
    private String email;

    private String phoneNumber;
}
