package com.spring.validateTokenservice.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import java.security.Key;

@Service
@PropertySource("classpath:application.properties")
public class ValitateTokenService {
    @Value("${jwt.secret-key}")
    String SECRET_KEY ="eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJJc3N1ZXIiOiJJc3N1ZXIiLCJVc2VybmFtZSI6IkphdmFJblVzZSIsImV4cCI6MTcwNjgyMDUwNywiaWF0IjoxNzA2ODIwNTA3fQ.2OTNSHysmHT62VmcOjSDxY-hZxTmoxfkfEOahY3A7_o";

    public boolean validateToken(String token) {
        Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
        try {
          Claims claims =  Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                            .getBody();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
