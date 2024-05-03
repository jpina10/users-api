package com.users.api.security;

import com.users.api.model.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;

@Getter
public class UserPrincipal {
    private static final String ROLE = "ROLE_";

    private final String username;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;

    private UserPrincipal(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();

        this.authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(ROLE.concat(role.name())))
                .toList();
    }

    public static UserPrincipal create(User user) {
        return new UserPrincipal(user);
    }
}
