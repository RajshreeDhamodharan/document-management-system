package com.example.backend.dto;

import com.example.backend.entity.Role;

public class UserResponseDTO {

    private Long id;

    private String name;

    private String email;

    private String phone;

    private Role role;

    private String department;

    private String status;

    // Default Constructor
    public UserResponseDTO() {
    }

    // Parameterized Constructor
    public UserResponseDTO(Long id,
                           String name,
                           String email,
                           String phone,
                           Role role,
                           String department,
                           String status) {

        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.department = department;
        this.status = status;
    }

    // =====================
    // Getters and Setters
    // =====================

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
}