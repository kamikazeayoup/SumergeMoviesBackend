package com.spring.validateTokenservice.controller;

import com.spring.validateTokenservice.service.ValitateTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ValidateTokenController {

    @Autowired
    private ValitateTokenService valitateTokenService;

    @GetMapping("/validate")
    @CrossOrigin(origins = "*", allowedHeaders = "*")

    public boolean Validate( String token){
        return valitateTokenService.validateToken(token);
    }
}
