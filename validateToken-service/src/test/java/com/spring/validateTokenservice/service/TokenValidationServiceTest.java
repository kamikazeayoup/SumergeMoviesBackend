package com.spring.validateTokenservice.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.TestPropertySources;

import java.security.Key;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest

@TestPropertySource("classpath:test-application.properties")
@ExtendWith(MockitoExtension.class)
public class TokenValidationServiceTest {

    @Value("${jwt.secret-key}")
    private String SECRET_KEY;
    @InjectMocks
    private ValitateTokenService valitateTokenService;

    @BeforeEach
    void init(){
        valitateTokenService.SECRET_KEY = SECRET_KEY;
    }


    @Test
    public void validateTokenSuccessTest() {
        System.out.println(SECRET_KEY);

             try(MockedStatic<Jwts> mockedJwts = Mockito.mockStatic(Jwts.class)) {
                 Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
                 Claims mockClaims = Mockito.mock(Claims.class);
                 JwtParserBuilder jwtParserBuilder = Mockito.mock(JwtParserBuilder.class);
                 JwtParser jwtParser = Mockito.mock(JwtParser.class);

                 mockedJwts.when(() -> Jwts.parserBuilder())
                         .thenReturn(jwtParserBuilder);
                 mockedJwts.when(() -> Jwts.parserBuilder().setSigningKey(key))
                         .thenReturn(jwtParserBuilder);
                 mockedJwts.when(() -> Jwts.parserBuilder().setSigningKey(key).build())
                         .thenReturn(jwtParser);

                 mockedJwts.when(() -> jwtParserBuilder.setSigningKey(key).build().parseClaimsJws("validToken"))
                         .thenReturn(Mockito.mock(Jws.class));
                 mockedJwts.when(() -> jwtParserBuilder.setSigningKey(key).build().parseClaimsJws("validToken").getBody())
                         .thenReturn(mockClaims);

                 boolean result = valitateTokenService.validateToken("validToken");
                 assertThat(result).isEqualTo(true);
                 assertThat(jwtParserBuilder.setSigningKey(key).build().parseClaimsJws("validToken").getBody()).isEqualTo(mockClaims);
             }

    }

    @Test
    public void validateTokenFailTest(){
            boolean result = valitateTokenService.validateToken("validToken");
            assertThat(result).isEqualTo(false);
    }
}
