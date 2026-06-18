package com.recordedapp.recordedback.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "users_db")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Username is required")
    private String username;

    @Column(nullable=false)
    @NotBlank(message = "Password is required")
    @Size(min =8, message = "Password must be at least 8 characters")
   // @JsonIgnore //keep password out of the response JSON while still saving to database llh4/10/2025
    @JsonProperty(access= JsonProperty.Access.WRITE_ONLY) //06_02_2025 add this instead of @JsonIgnore
    private String password; //will be encrypted later

    @Column(nullable=false)
    @NotBlank(message = "Email is required")
    @Email( message = "Email should be valid")
    private String email;

    @Column(name="last_name", nullable=false)
    @NotBlank(message = "Last name is required")
    private String lastName;

    @Column(name="first_name", nullable = false)
    @NotBlank(message = "First name is required")
    private String firstName;

    @Enumerated(EnumType.STRING)
    private Role role; //Example: ADMIN, USER

    public User(){}

    public User(Long id, String username, String password, String email, String lastName, String firstName, Role role){
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.lastName = lastName;
        this.firstName = firstName;
        this.role = role;
    }

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getLastName() {return lastName;}

    public void setLastName(String lastName) {this.lastName = lastName;}

    public String getFirstName() {return firstName;}

    public void setFirstName(String firstName) {this.firstName = firstName;}

    public Role getRole(){
        return role;
    }

    public void setRole(Role role){
        this.role = role;
    }


}
