package com.sumerge.authservice.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.AuthenticationException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class JwtEntryPointTest {
    @InjectMocks
    JwtAuthEntryPoint jwtEntryPoint;

    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCommence() throws Exception {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        AuthenticationException authenticationException = mock(AuthenticationException.class);

        jwtEntryPoint.commence(request, response, authenticationException);
        verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED , null);
    }
}
