package com.users.api.service;

import com.users.api.dto.AddressDto;
import com.users.api.dto.CreateAddressDto;
import com.users.api.exception.model.AddressNotFoundException;
import com.users.api.mapper.AddressMapper;
import com.users.api.model.Address;
import com.users.api.model.User;
import com.users.api.repository.AddressRepository;
import com.users.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    private final UserRepository userRepository;

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

        addressRepository.delete(address);
    }

    @Override
    public void addAddress(String username, String addressId) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
        Address address = addressRepository.findById(Long.valueOf(addressId)).orElseThrow(() -> new AddressNotFoundException(addressId));

        user.addAddress(address);


    }

}
