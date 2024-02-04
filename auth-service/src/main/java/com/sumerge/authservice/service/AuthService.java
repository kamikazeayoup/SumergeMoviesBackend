package com.sumerge.authservice.service;

import com.sumerge.authservice.dto.AuthResponseDto;
import com.sumerge.authservice.dto.LoginDto;
import com.sumerge.authservice.dto.RegisterDto;
import com.sumerge.authservice.model.Role;
import com.sumerge.authservice.model.UserEntity;
import com.sumerge.authservice.repository.RoleRepository;
import com.sumerge.authservice.repository.UserEntityRepository;
import com.sumerge.authservice.jwt.JwtGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



import java.util.Collections;

@Service
public class AuthService {
    @Autowired
    private UserEntityRepository userEntityRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtGenerator jwtGenerator;

    public ResponseEntity<String> register(RegisterDto registerDto) {
        try {
            if (userEntityRepository.existsByUsername(registerDto.getUsername())) {
                return new ResponseEntity<>("Username is taken!", HttpStatus.BAD_REQUEST);
            }
            if (userEntityRepository.existsByEmail(registerDto.getEmail())) {
                return new ResponseEntity<>("Email is taken!", HttpStatus.BAD_REQUEST);
            }
            UserEntity user = new UserEntity();
            user.setUsername(registerDto.getUsername());
            user.setEmail(registerDto.getEmail());
            user.setPassword(passwordEncoder.encode((registerDto.getPassword())));

            Role roles = roleRepository.findByName("USER").get();
            user.setRoles(Collections.singletonList(roles));

            userEntityRepository.save(user);
            return new ResponseEntity<>("User registered success!", HttpStatus.OK);

        }catch (Exception ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);

        }

    }
    public ResponseEntity<?> login(LoginDto loginDto) {
        if (loginDto.getEmail() == null || loginDto.getEmail().isEmpty() ||
                loginDto.getPassword() == null || loginDto.getPassword().isEmpty()) {
            return new ResponseEntity<>("Email or password cannot be empty", HttpStatus.BAD_REQUEST);
        }

try {

    Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    loginDto.getEmail(),
                    loginDto.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String token = jwtGenerator.generateToken(authentication);
    return new ResponseEntity<>(new AuthResponseDto(token), HttpStatus.OK);
} catch (AuthenticationException e) {
        return new ResponseEntity<>("Username or password is not correct", HttpStatus.UNAUTHORIZED);
    }

}
    }



