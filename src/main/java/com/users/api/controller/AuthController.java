package com.users.api.controller;

import com.users.api.dto.LoginRequest;
import com.users.api.dto.LoginResponse;
import com.users.api.exception.validation.SecurityInputValidationException;
import com.users.api.model.User;
import com.users.api.repository.UserRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@Tag(name = "Auth", description = "Auth")
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthController {

    private static final String WRONG_USERNAME_OR_PASSWORD = "Wrong username or password";
    private final JwtEncoder jwtEncoder;
    private final UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.username()).orElseThrow(() -> new UsernameNotFoundException(loginRequest.username()));

        if (!user.getPassword().equals(loginRequest.password())) {
            throw new SecurityInputValidationException(WRONG_USERNAME_OR_PASSWORD);
        }

        var now = Instant.now();
        var expiresIn = 300L;
        var claims = JwtClaimsSet.builder()
                .issuer("users-api")
                .subject(String.valueOf(user.getId()))
                .expiresAt(now.plusSeconds(expiresIn))
                .issuedAt(now)
                .build();

        String jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return ResponseEntity.ok(new LoginResponse(jwtValue, expiresIn));
    }
}
