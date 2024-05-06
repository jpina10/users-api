package com.users.api.service;

import com.users.api.dto.AddressDto;
import com.users.api.dto.CreateAddressDto;
import com.users.api.exception.model.AddressNotFoundException;
import com.users.api.mapper.AddressMapper;
import com.users.api.model.Address;
import com.users.api.model.User;
import com.users.api.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    @Override
    @Transactional
    public AddressDto createAddress(CreateAddressDto createAddressDto) {
        Address address = addressMapper.toEntity(createAddressDto);
        addressRepository.save(address);

        return addressMapper.toDto(address);
    }

    @Override
    @Transactional
    public void deleteAddress(Long id) {
        Address address = addressRepository.findById(id).orElseThrow(() -> new AddressNotFoundException(id.toString()));
        //delete from users while iterating a collection and prevent ConcurrentModificationException
        List<User> users = new ArrayList<>(address.getUsers());
        users.forEach(user -> user.removeAddress(address));

        addressRepository.delete(address);
    }
}
