package com.users.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Schema(name = "Street")
public class StreetDto {

    @Schema(description = "name of the street", example = "Place de la Mairie")
    private String name;

    @Schema(description = "number of the building", example = "6298")
    private String number;
}
