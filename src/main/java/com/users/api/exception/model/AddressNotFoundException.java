package com.users.api.exception.model;

public class AddressNotFoundException extends ResourceNotFoundException {

    private static final String ADDRESS_NOT_FOUND = "Address with id %s does not exist.";

    public AddressNotFoundException(String id) {
        super(String.format(ADDRESS_NOT_FOUND, id));
    }
}
