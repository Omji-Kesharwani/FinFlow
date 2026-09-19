package com.finflow.gateway_service.controller;

import com.finflow.gateway_service.security.JwtBlacklistService;
import com.finflow.gateway_service.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.Date;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtService jwtService ;
    private final JwtBlacklistService jwtBlacklistService ;

    public AuthController(JwtService jwtService, JwtBlacklistService jwtBlacklistService) {
        this.jwtService = jwtService;
        this.jwtBlacklistService = jwtBlacklistService;
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout (HttpServletRequest request)
    {
        String authHeader = request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer "))
        {
            return ResponseEntity.status(401)
                    .body("Authorization token is required");

        }

        String token = authHeader.substring(7);
        if(!jwtService.isValid(token))
        {
            return ResponseEntity
                    .status(401)
                    .body("Invalid Token");
        }

        Date expiration = jwtService.extractExpiration(token);

        long remainingMillis = expiration.getTime() - System.currentTimeMillis() ;

        Duration remainingTime = Duration.ofMillis(remainingMillis);
        jwtBlacklistService.blacklist(token,remainingTime);

        return ResponseEntity.ok("Logged out successfully");
    }
}
