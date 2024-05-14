package com.users.api.security.jwt;

import com.users.api.dto.LoginRequest;

public interface JwtService {
    String generateToken(LoginRequest loginRequest);
}
