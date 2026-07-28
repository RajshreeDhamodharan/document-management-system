package com.example.backend.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.backend.dto.ForgotPasswordRequestDTO;
import com.example.backend.dto.ResetPasswordRequestDTO;
import com.example.backend.dto.VerifyOtpRequestDTO;
import com.example.backend.entity.Otp;
import com.example.backend.entity.User;
import com.example.backend.repository.OtpRepository;
import com.example.backend.repository.UserRepository;

@Service
public class ForgotPasswordService {

    private final UserRepository userRepository;
    private final OtpRepository otpRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public ForgotPasswordService(UserRepository userRepository,
                                 OtpRepository otpRepository,
                                 EmailService emailService,
                                 PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.otpRepository = otpRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    // ==========================================
    // Send OTP
    // ==========================================
    public String sendOtp(ForgotPasswordRequestDTO request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("User not found with this email."));

        String otpValue = generateOtp();

        Otp otp = otpRepository.findByEmail(request.getEmail())
                .orElse(new Otp());

        otp.setEmail(user.getEmail());
        otp.setOtp(otpValue);
        otp.setVerified(false);
        otp.setExpiryTime(
                LocalDateTime.now()
                        .plusMinutes(10)
                        .toString());

        otpRepository.save(otp);

        emailService.sendOtpEmail(user.getEmail(), otpValue);

        return "OTP sent successfully.";
    }

    // ==========================================
    // Verify OTP
    // ==========================================
    public String verifyOtp(VerifyOtpRequestDTO request) {

        Otp otp = otpRepository
                .findByEmailAndOtp(
                        request.getEmail(),
                        request.getOtp())
                .orElseThrow(() ->
                        new RuntimeException("Invalid OTP."));

        if (LocalDateTime.now().isAfter(
                LocalDateTime.parse(otp.getExpiryTime()))) {

            throw new RuntimeException("OTP has expired.");
        }

        otp.setVerified(true);
        otpRepository.save(otp);

        return "OTP verified successfully.";
    }

    // ==========================================
    // Reset Password
    // ==========================================
    public String resetPassword(ResetPasswordRequestDTO request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("User not found."));

        Otp otp = otpRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("OTP not found."));

        if (!otp.isVerified()) {
            throw new RuntimeException("Please verify OTP first.");
        }

        user.setPassword(
                passwordEncoder.encode(request.getNewPassword()));

        userRepository.save(user);

        otpRepository.delete(otp);

        return "Password reset successfully.";
    }

    // ==========================================
    // Generate 6 Digit OTP
    // ==========================================
    private String generateOtp() {

        Random random = new Random();

        int otp = 100000 + random.nextInt(900000);

        return String.valueOf(otp);
    }
}