package com.spring.validateTokenservice.controller;

import com.spring.validateTokenservice.service.ValitateTokenService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@WebMvcTest(ValidateTokenController.class)

public class ValidateTokenControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    ValitateTokenService valitateTokenService;

    @Test
    void ValidateToken_True() throws Exception {
        Mockito.when(valitateTokenService.validateToken("token")).thenReturn(true);
        boolean result = valitateTokenService.validateToken("token");
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/validate?token=token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertTrue(result);

    }
    @Test
    void ValidateToken_false() throws Exception {
        Mockito.when(valitateTokenService.validateToken("valid_token")).thenReturn(false);
        boolean result = valitateTokenService.validateToken("valid_token");
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/validate?token=valid_token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertFalse(result);
    }

}
