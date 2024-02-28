package com.users.api.mapper;

import com.users.api.dto.AddressDto;
import com.users.api.dto.CreateAddressDto;
import com.users.api.model.Address;
import com.users.api.nameapi.model.Location;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface AddressMapper {

    @Mapping(target = "street", source = "createAddressDto.street.name")
    @Mapping(target = "number", source = "createAddressDto.street.number")
    Address toEntity(Location createAddressDto);

    @Mapping(target = "street.name", source = "street")
    @Mapping(target = "street.number", source = "number")
    AddressDto toDto(Address address);

    @Mapping(source = "street.name", target = "street")
    @Mapping(source = "street.number", target = "number")
    Address toEntity(AddressDto addressDto);

    Address toEntity(CreateAddressDto createAddressDto);

    CreateAddressDto toCreateAddressDto(Address address);
}
