package com.finflow.auth.controller;

import com.finflow.auth.service.AuthService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class TestController {

    private final AuthService authService ;

    public TestController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/api/test")
    public  String test(Authentication authentication){
        return "User ID: " + authentication.getName()
                + "\nEmail: " + authentication.getDetails()
                + "\nRoles: " + authentication.getAuthorities();
    }

    @PostMapping("/api/admin/users/{userId}/make-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String makeAdmin(@PathVariable UUID userId){
        authService.assignAdminRole(userId);
        return "User promoted to ADMIN";
    }

    @GetMapping("/api/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminTest() {
        return "Welcome Admin!";
    }
}
