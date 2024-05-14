package com.users.api.security.basic;

import com.users.api.exception.model.UserNotFoundException;
import com.users.api.exception.validation.SecurityInputValidationException;
import com.users.api.model.User;
import com.users.api.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

import static com.users.api.security.SecurityMessages.WRONG_USERNAME_OR_PASSWORD;

@Component
@RequiredArgsConstructor
public class CustomBasicAuthFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION = "Authorization";
    private static final String BASIC = "Basic ";
    private static final String EMPTY_STRING = "";
    private static final String DOUBLE_DOTS = ":";

    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (isBasicAuthentication(request)) {
            String[] credentials = extractCredentials(request);
            String username = credentials[0];
            String password = credentials[1];

            User user = getValidatedUser(username, password);

            setAuthentication(user);
        }

        filterChain.doFilter(request, response);
    }

    private User getValidatedUser(String username, String password) {
        User user = getUserByUsername(username);
        validatePassword(user, password);

        return user;
    }

    private void validatePassword(User user, String password) {
        if (!checkPassword(user.getPassword(), password)) {
            throw new SecurityInputValidationException(WRONG_USERNAME_OR_PASSWORD);
        }
    }

    private User getUserByUsername(String username) {
        return userRepository.findByUsernameWithRoles(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    private String[] extractCredentials(HttpServletRequest request) {
        String header = getHeader(request);
        String base64Credentials = header.replace(BASIC, EMPTY_STRING);
        String decodedCredentials = decodeBase64(base64Credentials);
        return decodedCredentials.split(DOUBLE_DOTS);
    }

    private void setAuthentication(User user) {
        Authentication authentication = createAuthenticationToken(user);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private Authentication createAuthenticationToken(User user) {
        UserPrincipal userPrincipal = UserPrincipal.create(user);

        return new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
    }

    private boolean checkPassword(String password, String loginPassword) {
        //return passwordEnconder().matches(password, loginPassword)
        return password.equals(loginPassword);
    }


    private String decodeBase64(String base64) {
        byte[] decodeBytes = Base64.getDecoder().decode(base64);

        return new String(decodeBytes);
    }

    private boolean isBasicAuthentication(HttpServletRequest request) {
        String header = getHeader(request);
        return header != null && header.startsWith(BASIC);
    }

    private String getHeader(HttpServletRequest request) {
        return request.getHeader(AUTHORIZATION);
    }

    //to be used when password are encrypted
    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
