package com.users.api.exception.model;

public class AddressAlreadyExistsException extends ResourceAlreadyExistsException {

    private static final String ADDRESS_ALREADY_EXISTS = "Address with postal code %s already exists";

    public AddressAlreadyExistsException(String value) {
        super(String.format(ADDRESS_ALREADY_EXISTS, value));
    }
}
