package com.spring.validateTokenservice.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import java.security.Key;

import static org.assertj.core.api.Assertions.assertThat;

@PropertySource("classpath:application.properties")
@ExtendWith(MockitoExtension.class)
public class TokenValidationServiceTest {

    @InjectMocks
    private ValitateTokenService valitateTokenService;


    String SECRET_KEY= "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJJc3N1ZXIiOiJJc3N1ZXIiLCJVc2VybmFtZSI6IkphdmFJblVzZSIsImV4cCI6MTcwNjgyMDUwNywiaWF0IjoxNzA2ODIwNTA3fQ.2OTNSHysmHT62VmcOjSDxY-hZxTmoxfkfEOahY3A7_o";

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

                 boolean result = valitateTokenService.validateToken("validToken");
                 assertThat(result).isEqualTo(true);
                 assertThat(jwtParserBuilder.setSigningKey(key).build().parseClaimsJws("validToken").getBody()).isEqualTo(mockClaims);
             }

    }

    @Test
    public void testValidateTokenFalse(){
            boolean result = valitateTokenService.validateToken("validToken");
            assertThat(result).isEqualTo(false);
    }
}
