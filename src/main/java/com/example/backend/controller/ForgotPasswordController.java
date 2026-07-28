package com.example.backend.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.example.backend.dto.ForgotPasswordRequestDTO;
import com.example.backend.dto.ResetPasswordRequestDTO;
import com.example.backend.dto.VerifyOtpRequestDTO;
import com.example.backend.service.ForgotPasswordService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
@Validated
public class ForgotPasswordController {

    private final ForgotPasswordService forgotPasswordService;

    public ForgotPasswordController(ForgotPasswordService forgotPasswordService) {
        this.forgotPasswordService = forgotPasswordService;
    }

    // ==========================================
    // Send OTP
    // ==========================================
    @PostMapping("/forgot-password")
    public String forgotPassword(
            @RequestBody
            @Valid
            ForgotPasswordRequestDTO request) {

        return forgotPasswordService.sendOtp(request);
    }

    // ==========================================
    // Verify OTP
    // ==========================================
    @PostMapping("/verify-otp")
    public String verifyOtp(
            @RequestBody
            @Valid
            VerifyOtpRequestDTO request) {

        return forgotPasswordService.verifyOtp(request);
    }

    // ==========================================
    // Reset Password
    // ==========================================
    @PostMapping("/reset-password")
    public String resetPassword(
            @RequestBody
            @Valid
            ResetPasswordRequestDTO request) {

        return forgotPasswordService.resetPassword(request);
    }

}