package com.finflow.auth.service;

import com.finflow.auth.dto.LoginRequest;
import com.finflow.auth.dto.LoginResponse;
import com.finflow.auth.dto.RegisterRequest;
import com.finflow.auth.dto.RegisterResponse;
import com.finflow.auth.entity.AuthUser;
import com.finflow.auth.entity.Role;
import com.finflow.auth.exception.EmailAlreadyExistsException;
import com.finflow.auth.exception.InvalidCredentialsException;
import com.finflow.auth.exception.RoleNotFoundException;
import com.finflow.auth.repository.AuthUserRepository;
import com.finflow.auth.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthUserRepository authUserRepository ;
    private final RoleRepository roleRepository ;
    private final PasswordEncoder passwordEncoder ;
    private final JwtService jwtService ;

    public RegisterResponse register(RegisterRequest request){
        String email = request.getEmail().trim().toLowerCase();

        if(authUserRepository.existsByEmail(email))
        {
            throw new EmailAlreadyExistsException("Email is already registered");
        }

        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(()-> new RoleNotFoundException("USER role not found"));

        OffsetDateTime now = OffsetDateTime.now();
        AuthUser user = new AuthUser();
        user.setEmail(email);
        user.setPasswordHash(
                passwordEncoder.encode(request.getPassword())
        );

        user.setStatus("ACTIVE");
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        user.getRoles().add(userRole);

        AuthUser savedUser = authUserRepository.save(user);

        return new RegisterResponse(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getStatus()
        );
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request){
        String email = request.getEmail()
                .trim()
                .toLowerCase();

        AuthUser user = authUserRepository.findByEmail(email)
                .orElseThrow(()-> new InvalidCredentialsException(
                        "Invalid email or password"
                ));

        if(!passwordEncoder.matches(request.getPassword(), user.getPasswordHash()))
        {
            throw new InvalidCredentialsException(
                    "Invalid email or password"
            );
        }

        if(!"ACTIVE".equals(user.getStatus()))
        {
            throw new InvalidCredentialsException(
                    "User account is not active"
            );
        }

        String token = jwtService.generateToken(user);

        return new LoginResponse(token,"Bearer");

    }

    @Transactional
    public void assignAdminRole(UUID userId){
        AuthUser user = authUserRepository.findById(userId)
                .orElseThrow(()->
                        new InvalidCredentialsException("User not found"));

        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseThrow(()->new RoleNotFoundException("ADMIN role not found"));

        user.getRoles().add(adminRole);
        authUserRepository.save(user);
    }
}
