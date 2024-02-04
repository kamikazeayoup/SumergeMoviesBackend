package com.sumerge.authservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sumerge.authservice.dto.AuthResponseDto;
import com.sumerge.authservice.dto.LoginDto;
import com.sumerge.authservice.dto.RegisterDto;
import com.sumerge.authservice.model.Role;
import com.sumerge.authservice.model.UserEntity;
import com.sumerge.authservice.repository.RoleRepository;
import com.sumerge.authservice.repository.UserEntityRepository;
import com.sumerge.authservice.jwt.JwtGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AuthServiceTest {
    @InjectMocks
    private AuthService userService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserEntityRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtGenerator jwtGenerator;


    List<Role>roles;
    UserEntity user;
    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        Role newRole = new Role();
        newRole.setId(1);
        newRole.setName("USER");
        roles = new ArrayList<>(Arrays.asList(newRole));
        user = new UserEntity();
        user.setId(1);
        user.setUsername("medo");
        user.setEmail("m@a.com");
        user.setPassword("12334");
        user.setRoles(roles);
    }
    @Test
    void loginMock()  {
        when(authenticationManager.authenticate(
                any(UsernamePasswordAuthenticationToken.class)
        )).thenAnswer(i -> i.getArguments()[0]);
        LoginDto loginDto = new LoginDto("m@a.com", "192009alaa");
        when(jwtGenerator.generateToken(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail() , loginDto.getPassword())
        ))).thenReturn("token");
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        Optional<UserEntity> mockedUser = userRepository.findById(1);
        assertThat(mockedUser.get().getId()).isEqualTo(user.getId());
        assertThat(mockedUser.get().getEmail()).isEqualTo(user.getEmail());
        assertThat(mockedUser.get().getUsername()).isEqualTo(user.getUsername());
        assertThat(mockedUser.get().toString()).isEqualTo(user.toString());
        List<Role> mockedRule = mockedUser.get().getRoles();

        assertThat(mockedRule.get(0).getName()).isEqualTo(roles.get(0).getName());
        assertThat(mockedRule.get(0).getId()).isEqualTo(roles.get(0).getId());
        assertThat(mockedRule.get(0).toString()).isEqualTo(roles.get(0).toString());




        AuthResponseDto response = new AuthResponseDto("token");
        assertThat(response).isEqualTo(userService.login(loginDto).getBody());
    }
    @Test
    void RegisterMock() {
        RegisterDto registerDto = new RegisterDto();
        registerDto.setEmail("m@a.com");
        registerDto.setUsername("username");
        registerDto.setPassword("password");
        when(userRepository.existsByUsername(registerDto.getUsername())).thenReturn(false);
        when(userRepository.existsByUsername(registerDto.getUsername())).thenReturn(false);
        UserEntity user = new UserEntity();
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        when(roleRepository.save(new Role(1 , "USER"))).thenReturn(new Role(1 , "USER"));
        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        assertThat(userRepository.findByEmail(user.getEmail())).isEqualTo(Optional.of(user));

    }
    @Test
    void loginUsernameOrPasswordNotCorrect() {
        LoginDto auth = new LoginDto();
        auth.setEmail(null);
        auth.setPassword("123");
        ResponseEntity failedResponse = new ResponseEntity<>("Email or password cannot be empty" , HttpStatus.BAD_REQUEST);
        assertEquals(failedResponse ,userService.login(auth) );
    }
    @Test
    void registerNotCorrect() {
        RegisterDto auth = new RegisterDto();
        auth.setUsername("sw");
        auth.setEmail(null);
        auth.setPassword("123");

        userService.register(auth);

        ResponseEntity failedResponse = new ResponseEntity<>("No value present" , HttpStatus.BAD_REQUEST);
        assertEquals(userService.register(auth), failedResponse);
    }


}
