package com.example.backend.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email cannot be empty")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Phone number cannot be empty")
    @Pattern(regexp = "\\d{10}", message = "Phone number must contain exactly 10 digits")
    private String phone;

    @Enumerated(EnumType.STRING)
    private Role role;

    @NotBlank(message = "Department cannot be empty")
    private String department;

    private String status;

    // ==========================================
    // Progressive Lockout Fields
    // ==========================================

    private int failedAttempts = 0;

    private boolean accountLocked = false;

    private LocalDateTime lockTime;

    // ==========================================
    // Default Constructor (Required by JPA)
    // ==========================================

    public User() {
    }

    // ==========================================
    // Parameterized Constructor
    // ==========================================

    public User(Long id,
                String name,
                String email,
                String password,
                String phone,
                Role role,
                String department,
                String status) {

        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = role;
        this.department = department;
        this.status = status;
    }

    // ==========================================
    // Getters and Setters
    // ==========================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // ==========================================
    // Failed Attempts
    // ==========================================

    public int getFailedAttempts() {
        return failedAttempts;
    }

    public void setFailedAttempts(int failedAttempts) {
        this.failedAttempts = failedAttempts;
    }

    // ==========================================
    // Account Locked
    // ==========================================

    public boolean isAccountLocked() {
        return accountLocked;
    }

    public void setAccountLocked(boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    // ==========================================
    // Lock Time
    // ==========================================

    public LocalDateTime getLockTime() {
        return lockTime;
    }

    public void setLockTime(LocalDateTime lockTime) {
        this.lockTime = lockTime;
    }
}