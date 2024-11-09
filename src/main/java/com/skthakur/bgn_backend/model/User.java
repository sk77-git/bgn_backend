package com.skthakur.bgn_backend.model;

import com.skthakur.bgn_backend.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    private final LocalDateTime createdAt = LocalDateTime.now();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "Full Name is required")
    private String fullName;
    @NotNull(message = "Email is required")
    private String email;
    private String phone;
    @NotBlank(message = "Password is required")
    private String password;
    @NotNull(message = "Role is required")
    @Enumerated(EnumType.STRING)
    private UserRole role;
    private LocalDateTime updatedAt;

    public User() {
    }

    public User(String fullName, String email, String phone, String password, UserRole role, LocalDateTime updatedAt) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.role = role;
        this.updatedAt = updatedAt;
    }
}
