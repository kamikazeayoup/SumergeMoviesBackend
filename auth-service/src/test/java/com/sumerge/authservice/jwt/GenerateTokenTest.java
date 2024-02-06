package com.sumerge.authservice.jwt;

import com.sumerge.authservice.dto.LoginDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestPropertySource;

import java.security.Key;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest

@TestPropertySource("classpath:test-application.properties")

public class GenerateTokenTest {
    @Value("${jwt.secret-key}")

    String SECRET_KEY;
    @Value("${jwt.expiration}")

    long JWT_EXPIRATION;
    @InjectMocks
    private JwtGenerator jwtGenerator;
    @Mock
    private AuthenticationManager authenticationManager;

    @BeforeEach
    public void init() {
        jwtGenerator.SECRET_KEY = SECRET_KEY;
        jwtGenerator.JWT_EXPIRATION = JWT_EXPIRATION;
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void generateTokenTest(){
        LoginDto loginDto = new LoginDto("m@a.com", "192009alaa" , "token");
        when(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(),
                        loginDto.getPassword()
                )
        )).thenAnswer(i -> i.getArguments()[0]);
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(),
                        loginDto.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
        jwtGenerator.generateToken(auth);
    }

    @Test
    public void validateTokenSuccessTest() {

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

           boolean result = jwtGenerator.validateToken("validToken");
           assertThat(result).isEqualTo(true);
           assertThat(jwtParserBuilder.setSigningKey(key).build().parseClaimsJws("validToken").getBody()).isEqualTo(mockClaims);

       }

    }
    @Test
    public void ValidateTokenFailedTest(){
        assertThrows(Exception.class , ()->{
            boolean result = jwtGenerator.validateToken("validToken");
            assertThat(result).isEqualTo(false);
        });

    }
    @Test
    public void getUsernameFromJwtSuccessTest() {

       try (MockedStatic<Jwts> mockedJwts = Mockito.mockStatic(Jwts.class)){
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

           String result = jwtGenerator.getUsernameFromJWT("validToken");
           assertThat(result).isEqualTo(null);
           assertThat(jwtParserBuilder.setSigningKey(key).build().parseClaimsJws("validToken").getBody()).isEqualTo(mockClaims);

       }



    }

}
