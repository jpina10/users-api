package com.users.api.service;

import com.users.api.model.Address;
import com.users.api.model.User;
import com.users.api.nameapi.model.Location;

public interface AddressService {
    Address createAddress(User user, Location location);
}
