package com.users.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
@Schema(name = "User")
public class UserDto {

    @Schema(description = "username of the user", example = "sadladybug779")
    private String username;

    @Schema(description = "email of the user", example = "miro.bernard@example.com")
    private String email;

    @Schema(description = "first name of the user", example = "Miro")
    private String firstName;

    @Schema(description = "last name of the user", example = "Bernard")
    private String lastName;

    @Schema(description = "phone number of the user", example = "079 046 97 17")
    private String phoneNumber;

    @Schema(description = "location of the user")
    private LocationDto location;
}
