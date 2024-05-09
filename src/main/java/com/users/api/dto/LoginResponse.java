package com.users.api.dto;

public record LoginResponse(String accessToken, Long expiresIn) {
}
