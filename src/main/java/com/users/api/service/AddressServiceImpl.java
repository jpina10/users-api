package com.users.api.service;

import com.users.api.exception.ResourceAlreadyExistsException;
import com.users.api.mapper.AddressMapper;
import com.users.api.model.Address;
import com.users.api.model.User;
import com.users.api.nameapi.model.Location;
import com.users.api.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    @Override
    @Transactional
    public Address createAddress(User user, Location location) {
        findAddress(location.getPostCode());

        Address address = addressMapper.toEntity(location);
        address.setUser(user);
        addressRepository.save(address);

        return address;
    }

    private void findAddress(String postCode) {
        addressRepository.findByPostCode(postCode).ifPresent(address -> {
            throw new ResourceAlreadyExistsException("Address with postal code " + postCode + " already exists");
        });
    }

}
