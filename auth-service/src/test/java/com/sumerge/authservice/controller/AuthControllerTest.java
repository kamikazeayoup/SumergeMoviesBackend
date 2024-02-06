package com.sumerge.authservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sumerge.authservice.dto.LoginDto;
import com.sumerge.authservice.dto.RegisterDto;
import com.sumerge.authservice.repository.UserEntityRepository;
import com.sumerge.authservice.jwt.JwtGenerator;
import com.sumerge.authservice.service.AuthService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc

public class AuthControllerTest {
    @Autowired
    transient MockMvc mockMvc;

    @MockBean
    transient UserEntityRepository userRepository;

    @MockBean
    AuthService authService;
    @MockBean
    JwtGenerator jwtGenerator;


    @Test
    void loginSuccessfulTest() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
    LoginDto auth = new LoginDto("m@a.com", "192009alaa" , "token");


    MvcResult response = mockMvc
            .perform(post("/api/auth/login")
                    .content(objectMapper.writeValueAsString(auth)).contentType(APPLICATION_JSON))
            .andReturn();
    assertThat(response.getResponse().getStatus()).isEqualTo(200);
}

@Test
    void registerSuccessfulTest() throws Exception{
    ObjectMapper objectMapper = new ObjectMapper();
    RegisterDto registerDto = new RegisterDto("medo" ,"medo@a.com", "12345678" , "token" );
    ResponseEntity responseEntity = new ResponseEntity<>("User registered success!", HttpStatus.OK);
    Mockito.when(authService.register(registerDto)).thenReturn(responseEntity);
    MvcResult response = mockMvc
            .perform(post("/api/auth/register")
                    .content(objectMapper.writeValueAsString(registerDto)).contentType(APPLICATION_JSON))
            .andReturn();

    assertThat(response.getResponse().getStatus()).isEqualTo(200);


}
}
