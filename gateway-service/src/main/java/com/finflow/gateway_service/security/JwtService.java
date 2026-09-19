package com.finflow.gateway_service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

    private final SecretKey secretKey ;

    public JwtService(@Value("${jwt.secret}") String secret){
        this.secretKey = Keys.hmacShaKeyFor(
                secret.getBytes()
        );
    }

    public UUID extractUserId(String token)
    {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload() ;

        return UUID.fromString(claims.getSubject());
    }

    public Date extractExpiration(String token)
    {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload() ;

        return claims.getExpiration() ;
    }

    public boolean isValid(String token)
    {
        try{
            extractUserId(token);
            return true ;
        }
        catch (Exception exception)
        {
            return false;
        }
    }
}
