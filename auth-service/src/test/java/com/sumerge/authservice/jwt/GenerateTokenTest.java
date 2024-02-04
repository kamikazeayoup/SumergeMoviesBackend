package com.sumerge.authservice.jwt;

import com.sumerge.authservice.dto.LoginDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Key;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class GenerateTokenTest {
    String SECRET_KEY = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJJc3N1ZXIiOiJJc3N1ZXIiLCJVc2VybmFtZSI6IkphdmFJblVzZSIsImV4cCI6MTcwNjgyMDUwNywiaWF0IjoxNzA2ODIwNTA3fQ.2OTNSHysmHT62VmcOjSDxY-hZxTmoxfkfEOahY3A7_o";

    @InjectMocks
    private JwtGenerator jwtGenerator;
    @Mock
    private AuthenticationManager authenticationManager;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void generateToken(){
        LoginDto loginDto = new LoginDto("m@a.com", "192009alaa");
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
    public void testValidateTokenSuccess() {

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
    public void testValidateTokenFalse(){
        assertThrows(Exception.class , ()->{
            boolean result = jwtGenerator.validateToken("validToken");
            assertThat(result).isEqualTo(false);
        });

    }
    @Test
    public void getUsernameFromJwtSuccess() {

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
