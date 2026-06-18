package com.recordedapp.recordedback.dtos;

import com.recordedapp.recordedback.models.User;


public class AuthResponseDTO {
    private String token;
    private String username; //llh 8-4-2025 adding for editing profile
    private String firstName; //llh 8-4-2025 adding for editing profile
    private String lastName; //llh 8-4-2025 adding for editing profile
    private String email; //llh 8-4-2025 adding for editing profile
    private String role; //llh 8-4-2025 adding for editing profile

    //llh 8-4-2025 added this constructor
    public AuthResponseDTO(){

    }

    //public AuthResponseDTO(String token){
    public AuthResponseDTO(String token, String username, String firstName, String lastName, String email, String role){
        this.token = token;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role =role;
    }

    public static AuthResponseDTO from(User user, String token) {
        return new AuthResponseDTO(
                token,
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole().name()
        );
    }


    public String getToken(){
        return token;
    }

    public void setToken(String token){
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getFirstName(){
        return firstName;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getRole(){
        return role;
    }

    public void setRole(String role){
        this.role = role;
    }
}
