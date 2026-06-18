package com.recordedapp.recordedback.controllers;

import com.recordedapp.recordedback.dtos.AuthResponseDTO;
import com.recordedapp.recordedback.dtos.UserLoginDTO;
import com.recordedapp.recordedback.dtos.UserRegisterDTO;
import com.recordedapp.recordedback.models.Role;
import com.recordedapp.recordedback.models.User;
import com.recordedapp.recordedback.repositories.UserRepository;
import com.recordedapp.recordedback.security.CustomUserDetailsService;
import com.recordedapp.recordedback.security.JwtTokenUtil;

import com.recordedapp.recordedback.services.TokenBlacklistService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
//@CrossOrigin(origins = "*") // allows frontend to call this backend from a different domain
//@CrossOrigin(origins = "http://localhost:5173") //5-25-2025
public class AuthController {

    // Inject dependencies
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ----------- REGISTER NEW USER ---------------
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRegisterDTO newUser) {
        // Check if username already exists
        if (userRepository.findByUsername(newUser.getUsername()).isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Username already taken");
        }

        // Create a new user and save it to the database
        User user = new User();
        user.setUsername(newUser.getUsername());
        user.setPassword(passwordEncoder.encode(newUser.getPassword())); // store hashed password
        user.setFirstName(newUser.getFirstName()); //llh 06-20-2025 add this to registration
        user.setLastName(newUser.getLastName()); //llh 06-20-2025 add this to registration
        user.setEmail(newUser.getEmail()); //llh 06-20-2025 add this to registration
        user.setRole(Role.USER); //llh 06-20-2025 add this to registration
        userRepository.save(user);

        // Generate a JWT token for the new user
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getUsername());
        String token = jwtTokenUtil.generateToken(userDetails);

        // Return the token as a response
        return ResponseEntity.ok(AuthResponseDTO.from(user, token)); //llh 8-5-2025 changed this to use from function defined in AuthResponseDTO (same as login method)
    }

    // ----------- LOGIN EXISTING USER ---------------
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginDTO loginData) {
        try {
            // Try to authenticate the user with the username and password
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginData.getUsername(),
                            loginData.getPassword()
                    )
            );

            //llh 8-5-2025 load full user entity so DTO can be populated
            User user = userRepository.findByUsername(loginData.getUsername())
                    .orElseThrow(() -> new RuntimeException("AuthController/login:User not found"));

            // If authentication is successful, load user details
            //UserDetails userDetails = userDetailsService.loadUserByUsername(loginData.getUsername());
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginData.getUsername());


            // Generate JWT token
            //String token = jwtTokenUtil.generateToken(userDetails.getUsername());
            String token = jwtTokenUtil.generateToken(userDetails);

            // Return the token as a response
            //return ResponseEntity.ok(new AuthResponseDTO(token));
            return ResponseEntity.ok(AuthResponseDTO.from(user, token)); //llh 8-5-2025 changed to this to get all the information for a user entity

        } catch (BadCredentialsException e) {
            // If login fails due to bad credentials, return error
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");
        }
    }

    //---- Logout user----
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {

        try{
            String authHeader = request.getHeader("Authorization");

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                tokenBlacklistService.blacklistToken(token);
                System.out.println("User logged out successfully");
                return ResponseEntity.ok().body("{\"message\":\"Logged out successfully\"}");
            }

            return ResponseEntity.badRequest().body("{\"error\":\"No token provided\"}");
        } catch(Exception e){
            return ResponseEntity.internalServerError().body("{\":\"Logout failed\"}");
        }

    }
}
