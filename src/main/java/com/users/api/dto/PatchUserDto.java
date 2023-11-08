package com.users.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Schema(name = "PatchUser")
public class PatchUserDto {

    @Schema(description = "username of the user", example = "sadladybug779")
    private String username;

    @Schema(description = "password of the user", example = "password")
    private String password;

    @Schema(description = "email of the user", example = "miro.bernard@example.com")
    private String email;

    @Schema(description = "first name of the user", example = "Miro")
    private String firstName;

    @Schema(description = "last name of the user", example = "Bernard")
    private String lastName;

    @Schema(description = "phone number of the user", example = "079 046 97 17")
    private String phoneNumber;

    @Schema(description = "street name", example = "1st Street")
    private String street;

    @Schema(description = "number of the house", example = "Switzerland")
    private String number;

    @Schema(description = "city name", example = "Wildberg")
    private String city;

    @Schema(description = "country name", example = "Switzerland")
    private String country;

    @Schema(description = "postal code", example = "9717")
    private String postCode;
}
