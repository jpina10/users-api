package com.users.api.service;

import com.users.api.dto.AddressDto;
import com.users.api.dto.CreateAddressDto;

public interface AddressService {
    AddressDto createAddress(CreateAddressDto createAddressDto);

    void deleteAddress(Long id);

    void addAddress(String username, String addressId);
}
