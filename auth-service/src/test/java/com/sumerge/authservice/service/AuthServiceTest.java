package com.sumerge.authservice.service;

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
import org.springframework.security.core.AuthenticationException;
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
    @Mock
    private CaptchaService captchaService;


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
    void login_SuccessTest()  {
        when(authenticationManager.authenticate(
                any(UsernamePasswordAuthenticationToken.class)
        )).thenAnswer(i -> i.getArguments()[0]);

        LoginDto loginDto = new LoginDto("m@a.com", "192009alaa" , "captoken");
        loginDto.setCaptchaToken("captoken");
        when(jwtGenerator.generateToken(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail() , loginDto.getPassword())
        ))).thenReturn("token");

        when(captchaService.verify("captoken")).thenReturn(true);
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
    void login_FailedWrongCaptchaTest()  {
        when(authenticationManager.authenticate(
                any(UsernamePasswordAuthenticationToken.class)
        )).thenAnswer(i -> i.getArguments()[0]);

        LoginDto loginDto = new LoginDto("m@a.com", "192009alaa" , "captoken");
        loginDto.setCaptchaToken("captoken");
        when(jwtGenerator.generateToken(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail() , loginDto.getPassword())
        ))).thenReturn("token");

        when(captchaService.verify("captoken")).thenReturn(false);
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

        assertThat(userService.login(loginDto).getBody()).isEqualTo("Invalid Captcha!");
    }

    @Test
    void login_FailedUsernameOrPasswordisNotCorrectTest() {
        LoginDto loginDto = new LoginDto("a@", "192009aaa" , "captoken");
        loginDto.setCaptchaToken("captoken");
        when(captchaService.verify("captoken")).thenReturn(true);

        when(authenticationManager.authenticate(any()))
                .thenThrow(new AuthenticationException("Test AuthenticationException") {});

       assertThat(userService.login(loginDto).getBody()).isEqualTo("Username or password is not correct");
    }
    @Test
    void login_FailedUsernameOrPasswordCannotBeEmptyTest() {
        LoginDto auth = new LoginDto();
        auth.setEmail(null);
        auth.setPassword("123");
        auth.setCaptchaToken("token");
        when(captchaService.verify("token")).thenReturn(true);
        ResponseEntity failedResponse = new ResponseEntity<>("Email or password cannot be empty" , HttpStatus.BAD_REQUEST);
        assertEquals(failedResponse ,userService.login(auth) );
    }

    @Test
    void register_FailedInvalidTokenTest() {
        RegisterDto registerDto = new RegisterDto();
        registerDto.setEmail("m@a.com");
        registerDto.setUsername("username");
        registerDto.setPassword("password");
        registerDto.setCaptchaToken("captoken");
        UserEntity user = new UserEntity();
        when(captchaService.verify("captoken")).thenReturn(false);
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));


        assertThat(userService.register(registerDto).getBody()).isEqualTo("Invalid Captcha!");

    }

    @Test
    void register_SuccessTest() {
        RegisterDto registerDto = new RegisterDto();
        registerDto.setEmail("m@a.com");
        registerDto.setUsername("username");
        registerDto.setPassword("password");
        registerDto.setCaptchaToken("captoken");
        UserEntity user = new UserEntity();
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        Role role = new Role(1 , "USER");
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setRoles(roles);
        //Mocking
        when(captchaService.verify("captoken")).thenReturn(true);
        when(userRepository.existsByUsername(registerDto.getUsername())).thenReturn(false);
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(role));
        when(roleRepository.save(role)).thenReturn(role);
        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        //Assertions
        ResponseEntity result = userService.register(registerDto);
        assertThat(result.getBody()).isEqualTo("User registered success!");
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(userRepository.findByEmail(user.getEmail())).isEqualTo(Optional.of(user));


    }

    @Test
    void register_FailedUsernameIsTaken() {
        RegisterDto registerDto = new RegisterDto();
        registerDto.setEmail("m@a.com");
        registerDto.setUsername("username");
        registerDto.setPassword("password");
        registerDto.setCaptchaToken("captoken");
        UserEntity user = new UserEntity();
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setRoles(roles);
        //Mocking
        when(captchaService.verify("captoken")).thenReturn(true);
        when(userRepository.existsByUsername(registerDto.getUsername())).thenReturn(true);
        //Assertions
        ResponseEntity result = userService.register(registerDto);
        assertThat(result.getBody()).isEqualTo("Username is taken!");
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }

    @Test
    void register_FailedEmailIsTaken() {
        RegisterDto registerDto = new RegisterDto();
        registerDto.setEmail("m@a.com");
        registerDto.setUsername("username");
        registerDto.setPassword("password");
        registerDto.setCaptchaToken("captoken");
        UserEntity user = new UserEntity();
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setRoles(roles);
        //Mocking
        when(captchaService.verify("captoken")).thenReturn(true);
        when(userRepository.existsByUsername(registerDto.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(registerDto.getEmail())).thenReturn(true);

        //Assertions
        ResponseEntity result = userService.register(registerDto);
        assertThat(result.getBody()).isEqualTo("Email is taken!");
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }
    @Test
    void register_NotCorrectTest() {
        RegisterDto auth = new RegisterDto();
        auth.setUsername("sw");
        auth.setEmail(null);
        auth.setPassword("123");
        auth.setCaptchaToken("token");
        when(captchaService.verify("token")).thenReturn(true);

        userService.register(auth);

        ResponseEntity failedResponse = new ResponseEntity<>("No value present" , HttpStatus.BAD_REQUEST);
        assertEquals(userService.register(auth), failedResponse);
    }

}
