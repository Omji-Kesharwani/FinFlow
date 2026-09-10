package com.finflow.auth.service;

import com.finflow.auth.entity.AuthUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class JwtService {
    private final SecretKey secretKey ;
    private final long expiration ;

    public JwtService(@Value("${jwt.secret}") String secret , @Value("${jwt.expiration}") long expiration){
        this.secretKey = Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );

        this.expiration  = expiration ;
     }

     public String generateToken(AuthUser user){
        String roles = user.getRoles()
                .stream()
                .map(role->role.getName())
                .collect(Collectors.joining(","));

        Date now = new Date();

        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("email",user.getEmail())
                .claim("roles",roles)
                .issuedAt(now)
                .expiration(new Date(now.getTime()+expiration))
                .signWith(secretKey)
                .compact() ;
     }

     public Jws<Claims> validateToken(String token){
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token);
     }
}
