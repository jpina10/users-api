package com.users.api.service;

import com.users.api.dto.CreateAddressDto;
import com.users.api.factory.TestFactory;
import com.users.api.mapper.AddressMapper;
import com.users.api.model.Address;
import com.users.api.repository.AddressRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddressServiceImplTest {

    private final TestFactory testFactory = new TestFactory();

    @Mock
    private AddressMapper addressMapper;

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private AddressServiceImpl addressService;

    @Test
    void createAddress() {
        CreateAddressDto createAddressDto = testFactory.getCreateAddressDto();
        Address address = testFactory.getAddress();

        when(addressMapper.toEntity(createAddressDto)).thenReturn(address);

        addressService.createAddress(createAddressDto);

        verify(addressMapper).toEntity(createAddressDto);
        verify(addressRepository).save(address);
    }

    @Test
    void deleteAddress() {
        Address address = testFactory.getAddress();

        when(addressRepository.findById(any(Long.class))).thenReturn(Optional.of(address));

        addressService.deleteAddress(address.getId());

        verify(addressRepository).delete(address);
    }
}