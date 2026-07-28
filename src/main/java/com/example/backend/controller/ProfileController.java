package com.example.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.backend.dto.ChangePasswordDTO;
import com.example.backend.dto.ProfileResponseDTO;
import com.example.backend.dto.UpdateProfileDTO;
import com.example.backend.service.ProfileService;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    // View Profile
    @GetMapping
    public ResponseEntity<ProfileResponseDTO> getProfile() {
        return ResponseEntity.ok(profileService.getProfile());
    }

    // Update Profile
    @PutMapping
    public ResponseEntity<ProfileResponseDTO> updateProfile(
            @RequestBody UpdateProfileDTO dto) {

        return ResponseEntity.ok(profileService.updateProfile(dto));
    }

    // Change Password
    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @RequestBody ChangePasswordDTO dto) {

        return ResponseEntity.ok(profileService.changePassword(dto));
    }
}