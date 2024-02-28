package com.users.api.service;

import com.users.api.dto.AddressDto;
import com.users.api.dto.CreateAddressDto;
import com.users.api.exception.ResourceAlreadyExistsException;
import com.users.api.exception.ResourceNotFoundException;
import com.users.api.mapper.AddressMapper;
import com.users.api.model.Address;
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
    public AddressDto createAddress(CreateAddressDto createAddressDto) {
        findAddress(createAddressDto.getPostCode());

        Address address = addressMapper.toEntity(createAddressDto);
        addressRepository.save(address);

        return addressMapper.toDto(address);
    }

    @Override
    @Transactional
    public void removeAddress(Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address with id " + id + "doesn't exist"));

        addressRepository.delete(address);
    }

    private void findAddress(String postCode) {
        addressRepository.findByPostCode(postCode).ifPresent(address -> {
            throw new ResourceAlreadyExistsException("Address with postal code " + postCode + " already exists");
        });
    }

}
