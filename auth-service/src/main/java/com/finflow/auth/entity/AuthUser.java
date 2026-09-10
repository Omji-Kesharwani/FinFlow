package com.finflow.auth.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "auth_users")
@Entity
public class AuthUser {

    @Id
    @GeneratedValue
    private UUID id ;

    @Column(nullable = false , unique = true ,length = 255)
    private String email ;

    @Column(name = "password_hash" , nullable = false ,length = 255)
    private String passwordHash ;

    @Column(nullable = false ,length = 20)
    private String status ;

    @Column(name = "created_at" , nullable = false)
    private OffsetDateTime createdAt ;

    @Column(name = "updated_at" , nullable = false)
    private OffsetDateTime updatedAt ;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )

    private Set<Role> roles = new HashSet<>() ;
}
