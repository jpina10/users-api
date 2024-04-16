package com.users.api.controller;

import com.users.api.security.Secured;
import com.users.api.dto.CreateUserResponse;
import com.users.api.dto.UserDto;
import com.users.api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.json.JsonPatch;
import java.util.List;

@RestController
@Tag(name = "Users", description = "Users")
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Secured
public class UserController {

    private final UserService userService;

    @Operation(summary = "Retrieves a User given a username")
    @GetMapping(value = "/{username}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The User has been returned", content = @Content(schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "404", description = "The User has not been found", content = @Content)})
    public UserDto getUserByUsername(@Parameter(name = "username") @PathVariable String username) {
        return userService.findUserByUserName(username);
    }


    @Operation(summary = "Creates a User calling a 3rd party API")
    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "The User has been created"),
            @ApiResponse(responseCode = "503", description = "Random user API not available")
    })
    public ResponseEntity<CreateUserResponse> createRandomUser() {
        String randomUserUsername = userService.createRandomUser();

        return new ResponseEntity<>(new CreateUserResponse(randomUserUsername), HttpStatus.CREATED);
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{username}")
    public void deleteUser(@Parameter(name = "username") @PathVariable String username) {
        userService.deleteUser(username);
    }

    @PutMapping("/{username}")
    public void enableUser(@Parameter(name = "username") @PathVariable String username) {
        userService.enableUser(username);
    }

    @PatchMapping(value = "/{username}", consumes = "application/json-patch+json")
    public void updateUser(@Parameter(name = "username") @PathVariable String username, @RequestBody JsonPatch jsonPatch) {
        userService.updateUser(username, jsonPatch);
    }

    @PostMapping("/admin")
    public void createAdminUser() {
        userService.createAdminUser();
    }
}
