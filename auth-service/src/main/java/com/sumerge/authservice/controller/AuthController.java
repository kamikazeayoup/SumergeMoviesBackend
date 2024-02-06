package com.sumerge.authservice.controller;

import com.sumerge.authservice.dto.LoginDto;
import com.sumerge.authservice.dto.RegisterDto;
import com.sumerge.authservice.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@CrossOrigin
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;
    @CrossOrigin

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {
        return authService.register(registerDto);
    }
    @CrossOrigin

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto) {
            return authService.login(loginDto);
    }

}
