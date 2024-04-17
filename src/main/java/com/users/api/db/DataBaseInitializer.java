package com.users.api.db;

import com.users.api.model.Role;
import com.users.api.model.User;
import com.users.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
@Profile("dev")
public class DataBaseInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    @Override
    public void run(String... args) {
        addAdminUser();
    }

    private void addAdminUser() {
        User user = new User();
        user.setUsername("admin");
        user.setPassword("admin");
        user.setRoles(Set.of(Role.ADMIN));

        userRepository.save(user);
    }
}
