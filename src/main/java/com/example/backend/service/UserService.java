package com.example.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.backend.dto.LoginRequest;
import com.example.backend.dto.LoginResponse;
import com.example.backend.dto.UserRequestDTO;
import com.example.backend.dto.UserResponseDTO;
import com.example.backend.entity.Role;
import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.security.JwtUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.time.Duration;
import java.time.LocalDateTime;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;
    // ==========================================
// Pagination
// ==========================================

public Page<UserResponseDTO> getUsers(int page, int size) {

    Pageable pageable = PageRequest.of(page, size);

    return userRepository.findAll(pageable)
            .map(this::convertToResponse);
}
  // ==========================================
// Get Users with Pagination
// ==========================================


    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil,
                       EmailService emailService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.emailService = emailService;
    }

    // ==========================
    // Register User
    // ==========================
    public UserResponseDTO registerUser(UserRequestDTO dto) {

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already exists.");
        }

        User user = new User();

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setPhone(dto.getPhone());
        user.setDepartment(dto.getDepartment());

        if (dto.getRole() == null) {
            user.setRole(Role.GUEST);
        } else {
            user.setRole(dto.getRole());
        }

        if (dto.getStatus() == null || dto.getStatus().isBlank()) {
            user.setStatus("ACTIVE");
        } else {
            user.setStatus(dto.getStatus());
        }

        User savedUser = userRepository.save(user);

        // Send Welcome Email
        emailService.sendRegistrationEmail(
                savedUser.getEmail(),
                savedUser.getName());

        return convertToResponse(savedUser);
    }
    private int getLockMinutes(int failedAttempts) {

    if (failedAttempts >= 15) {
        return 1440; // 24 hours
    }

    if (failedAttempts >= 10) {
        return 30;
    }

    if (failedAttempts >= 5) {
        return 15;
    }

    return 0;
}
    // ==========================
    // Login
    // ==========================
  public LoginResponse loginUser(LoginRequest request) {

    User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() ->
                    new RuntimeException("User not found"));

    // ==========================
    // Check Lock Status
    // ==========================

    if (user.isAccountLocked()) {

        int lockMinutes = getLockMinutes(user.getFailedAttempts());

        LocalDateTime unlockTime =
                user.getLockTime().plusMinutes(lockMinutes);

        if (LocalDateTime.now().isBefore(unlockTime)) {

            long remaining =
                    Duration.between(
                            LocalDateTime.now(),
                            unlockTime)
                            .toMinutes();

            return new LoginResponse(
                    "Account locked. Try again after "
                            + remaining + " minute(s).",
                    false,
                    null);
        }

        // Unlock account automatically

        user.setAccountLocked(false);
        user.setFailedAttempts(0);
        user.setLockTime(null);

        userRepository.save(user);
    }

    // ==========================
    // Password Validation
    // ==========================

    if (!passwordEncoder.matches(
            request.getPassword(),
            user.getPassword())) {

        int attempts = user.getFailedAttempts() + 1;

        user.setFailedAttempts(attempts);

        if (attempts >= 5) {

            user.setAccountLocked(true);
            user.setLockTime(LocalDateTime.now());

            userRepository.save(user);

            int lockMinutes = getLockMinutes(attempts);

            return new LoginResponse(
                    "Too many failed attempts. Account locked for "
                            + lockMinutes + " minutes.",
                    false,
                    null);
        }

        userRepository.save(user);

        return new LoginResponse(
                "Invalid password. Remaining attempts: "
                        + (5 - attempts),
                false,
                null);
    }

    // ==========================
    // Successful Login
    // ==========================

    user.setFailedAttempts(0);
    user.setAccountLocked(false);
    user.setLockTime(null);

    userRepository.save(user);

    String token = jwtUtil.generateToken(
        user.getEmail(),
        user.getRole()
);

    return new LoginResponse(
            "Login Successful",
            true,
            token);
}

    // ==========================
    // Get All Users
    // ==========================
    public List<UserResponseDTO> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // ==========================
    // Get User By ID
    // ==========================
    public UserResponseDTO getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        return convertToResponse(user);
    }

    // ==========================
    // Get User By Email
    // ==========================
    public UserResponseDTO getUserByEmail(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        return convertToResponse(user);
    }

    // ==========================
    // Update User
    // ==========================
    public UserResponseDTO updateUser(Long id, UserRequestDTO dto) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setDepartment(dto.getDepartment());
        user.setRole(dto.getRole());
        user.setStatus(dto.getStatus());

        if (dto.getPassword() != null &&
                !dto.getPassword().isBlank()) {

            user.setPassword(
                    passwordEncoder.encode(dto.getPassword()));
        }

        User updated = userRepository.save(user);

        return convertToResponse(updated);
    }

    // ==========================
    // Delete User
    // ==========================
    public void deleteUser(Long id) {

        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }

        userRepository.deleteById(id);
    }

    // ==========================
    // Entity -> Response DTO
    // ==========================
    private UserResponseDTO convertToResponse(User user) {

        UserResponseDTO dto = new UserResponseDTO();

        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setRole(user.getRole());
        dto.setDepartment(user.getDepartment());
        dto.setStatus(user.getStatus());

        return dto;
    }
}