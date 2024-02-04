package com.sumerge.authservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginDto {
    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;
    private String password;
    public LoginDto(){

    }
    public LoginDto(String _email , String _password){
        email = _email;
        password = _password;
    }
}
