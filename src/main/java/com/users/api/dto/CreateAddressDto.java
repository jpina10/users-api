package com.users.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateAddressDto {
    private String postCode;
    private String street;
    private String number;
    private String city;
    private String country;
}
