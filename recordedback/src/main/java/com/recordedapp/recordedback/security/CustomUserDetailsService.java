package com.recordedapp.recordedback.security;

import com.recordedapp.recordedback.models.User;
import com.recordedapp.recordedback.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    //Load user from  for Spring Security
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Step 1: Try to find the user in the repository by username. Using Optional to avoid nullpointer exception
        Optional<User> optionalUser = userRepository.findByUsername(username);

        // Step 2: If the user is not found, throw an exception
        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("user not found: " + username);
        }

        // Step 3: Extract the User from the Optional
        User user = optionalUser.get();

        // Step 4: Build the UserDetails object using the found user's data and this was coded with complete package path
        // like below to avoid confusion with the entity class called User
        UserBuilder userBuilder = org.springframework.security.core.userdetails.User.withUsername(user.getUsername());
        userBuilder.password(user.getPassword());
        userBuilder.authorities("USER");

        // Step 5: Return the built UserDetails object
        return userBuilder.build();

        /* same as code above but more explanations
        User User = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("user not found: "+username));

        return User.withUsername(User.getUsername())
                .password(User.getPassword())
                .authorities("USER")
                .build();

         */
    }
}
