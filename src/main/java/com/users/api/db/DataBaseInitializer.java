package com.users.api.db;

import com.users.api.model.Role;
import com.users.api.model.User;
import com.users.api.repository.RoleRepository;
import com.users.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Profile("dev")
public class DataBaseInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        User user = new User();
        user.setUsername("admin");
        user.setPassword("admin");

        Role role = new Role();
        role.setName("ADMIN");

        role.setUsers(List.of(user));
        roleRepository.save(role);

        userRepository.save(user);
    }
}
