package com.recordedapp.recordedback.controllers;

import com.recordedapp.recordedback.models.Role;
import com.recordedapp.recordedback.models.User;
import com.recordedapp.recordedback.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    //llh 04/2025
   /* @PostMapping("/create")
    public ResponseEntity<User> registerUser(@Valid @RequestBody User user){
        if (user.getRole() == null){
            user.setRole(Role.USER);
        }
        User savedUser = userService.saveUser(user);
        return ResponseEntity.ok(savedUser);
    }

    //llh 06-02-2025
    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@Valid @RequestBody User user){

        try {
            //check if required fields are present
            if (user.getUsername() == null || user.getPassword() == null) {
                System.err.println("Login failed: username or password is missing");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            //call service to authenticate user
            User authenticatedUser = userService.authenticateUser(user.getUsername(), user.getPassword());

            //if authentication is successful
            if (authenticatedUser != null) {
                return ResponseEntity.ok(authenticatedUser);
            } else {
                //authentication failed
                System.err.println("Login failed: Invalid credentials for user: " + user.getUsername());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            // Log the error for debugging
            System.err.println("Error during login: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        }
    }*/

}
