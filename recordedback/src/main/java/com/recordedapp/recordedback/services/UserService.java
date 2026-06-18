package com.recordedapp.recordedback.services;

import com.recordedapp.recordedback.repositories.UserRepository;
import com.recordedapp.recordedback.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/* LLHalpin created 3/2025: class that implements the business logic. Methods that implements communicating
 * with the UserRepository (class that communicates with the database)
 */
@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    //private final BCryptPasswordEncoder passwordEncoder;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    //llh 04/13/2025 update saveUser method to check if username exist before saving it for registering a new user
    public User saveUser(User user) {
        try {
            logger.info("Attempting to save user: {}", user.getUsername());

            //Check if username already exists
            Optional<User> existingUser = userRepository.findByUsername(user.getUsername());
            if (existingUser.isPresent()) {
                logger.warn("Username already exists: {}", user.getUsername());
                throw new IllegalArgumentException("Username already exists");
            }

            //Hash the password before saving - CRITICAL for security
            String originalPassword = user.getPassword();
            String hashedPassword = passwordEncoder.encode(originalPassword);
            user.setPassword(hashedPassword);

            logger.debug("Password hashed successfully for user: {}",user.getUsername());

            //save the user with hashed password
            User savedUser = userRepository.save(user);
            logger.info("User saved successfully: {}", savedUser.getUsername());

            return savedUser;

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e){
            logger.error("Error saving user: {}", user.getUsername(), e);
            throw new RuntimeException("Failed to save user: "+ e.getMessage());
        }
    }

    //Authenticate user by username and password
    public User authenticateUser(String username, String password) {
        try {
            logger.debug("Attempting to authenticate user: {}", username);

            //Basic input validation
            if (username == null || username.trim().isEmpty()) {
                logger.warn("Authentication failed: username is empty");
                return null;
            }

            //Find the user by username
            Optional<User> userOptional = userRepository.findByUsername(username.trim());

            if (userOptional.isEmpty()) {
                logger.info("Authentication failed: user not found: {}", username);
                return null;
            }

            User user = userOptional.get();

            //check if the provided password matches the stored hashed password
            boolean passwordMatches = passwordEncoder.matches(password, user.getPassword());

            if (passwordMatches) {
                logger.info("Authentication successful for user: {}", username);
                return user; //Login successful
            } else {
                logger.info("Authentication failed: Invalid password for user: {}",username);
                return null; //wrong password
            }
        } catch (Exception e){
            logger.error("Error during authentication for user: {}", username, e);
            return null;
        }
    }

    // BONUS METHOD: Check if username exists (useful for frontend validation)
    public boolean usernameExists(String username) {
        try {
            return userRepository.findByUsername(username).isPresent();
        } catch (Exception e) {
            logger.error("Error checking username existence: {}", username, e);
            return false;
        }
    }
}
