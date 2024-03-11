package com.users.api.config.security;

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
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomBasicAuthFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION = "Authorization";
    public static final String BASIC = "Basic ";

    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (isBasicAuthentication(request)) {
            String[] credentials = decodeBase64(getHeader(request).replace(BASIC, "")).split(":");

            String username = credentials[0];
            String password = credentials[1];

            //need this because the user is passed to another method and since it's a lazy many to many the roles are not loaded
            Optional<User> userOptional = userRepository.findByUsernameWithRoles(username);

            if(userOptional.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("User does not exist");
                return;
            }

            User user = userOptional.get();

            boolean valid = checkPassword(user.getPassword(), password);

            if(!valid) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Incorrect password");
            }

            setAuthentication(user);
        }

        filterChain.doFilter(request, response);
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
