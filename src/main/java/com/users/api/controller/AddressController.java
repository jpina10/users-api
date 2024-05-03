package com.users.api.controller;

import com.users.api.dto.AddressDto;
import com.users.api.dto.CreateAddressDto;
import com.users.api.security.Secured;
import com.users.api.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Addresses", description = "Addresses")
@RequiredArgsConstructor
@RequestMapping("/api/v1/address")
@Secured
public class AddressController {

    private final AddressService addressService;

    @Operation(summary = "Creates an Address")
    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "The Address has been created"),
    })
    public ResponseEntity<AddressDto> createAddress(@Valid @RequestBody CreateAddressDto createAddressDto) {
        AddressDto addressCreated = addressService.createAddress(createAddressDto);

        return new ResponseEntity<>(addressCreated, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{addressId}")
    public void deleteUser(@Parameter(name = "username") @PathVariable Long addressId) {
        addressService.deleteAddress(addressId);
    }
}
