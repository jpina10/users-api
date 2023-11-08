package com.users.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(name = "Location")
public class LocationDto {

    @Schema(description = "street of the user", example = "1st Street")
    private String street;

    private String number;

    @Schema(description = "city of the user", example = "Wildberg")
    private String city;

    @Schema(description = "country of the user", example = "Switzerland")
    private String country;

    @Schema(description = "postal code of the user", example = "9717")
    private String postCode;
}
