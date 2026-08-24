package com.example.backend.controller;

import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.example.backend.dto.LoginRequest;
import com.example.backend.dto.LoginResponse;
import com.example.backend.dto.UserRequestDTO;
import com.example.backend.dto.UserResponseDTO;
import com.example.backend.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
@Validated
public class AuthController {

    private final UserService userService;

    // Constructor Injection
    public AuthController(UserService userService) {
        this.userService = userService;
    }
     // ==========================================
// Get Users with Pagination
// ==========================================

@GetMapping("/users/page")
public ResponseEntity<Page<UserResponseDTO>> getUsers(

        @RequestParam(defaultValue = "0") int page,

        @RequestParam(defaultValue = "10") int size) {

    return ResponseEntity.ok(
            userService.getUsers(page, size));
}
    // ==========================
    // Register User
    // ==========================
    @PostMapping("/register")
    public UserResponseDTO registerUser(
            @Valid @RequestBody UserRequestDTO userRequestDTO) {

        return userService.registerUser(userRequestDTO);
    }

    // ==========================
    // Login User
    // ==========================
    @PostMapping("/login")
    public LoginResponse loginUser(
            @RequestBody LoginRequest request) {

        return userService.loginUser(request);
    }

    // ==========================
    // Get All Users
    // ==========================
    @GetMapping("/users")
    public List<UserResponseDTO> getAllUsers() {

        return userService.getAllUsers();
    }

    // ==========================
    // Get User By ID
    // ==========================
    @GetMapping("/user/{id}")
    public UserResponseDTO getUserById(
            @PathVariable Long id) {

        return userService.getUserById(id);
    }

    // ==========================
    // Get User By Email
    // ==========================
    @GetMapping("/email/{email}")
    public UserResponseDTO getUserByEmail(
            @PathVariable String email) {

        return userService.getUserByEmail(email);
    }

    // ==========================
    // Update User
    // ==========================
    @PutMapping("/update/{id}")
    public UserResponseDTO updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRequestDTO dto) {

        return userService.updateUser(id, dto);
    }

    // ==========================
    // Delete User
    // ==========================
    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {

        userService.deleteUser(id);

        return "User deleted successfully!";
    }
}