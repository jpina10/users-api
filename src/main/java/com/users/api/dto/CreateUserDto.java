package com.users.api.dto;

import com.users.api.util.validator.password.ValidPassword;
import com.users.api.util.validator.ValidationMessages;
import com.users.api.util.validator.user.ValidUsername;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateUserDto {

    @ValidUsername
    private String username;

    @ValidPassword
    private String password;

    private String firstName;
    private String lastName;

    @NotBlank(message = ValidationMessages.CANNOT_BE_NULL_OR_EMPTY)
    private String email;

    private String phoneNumber;
}
