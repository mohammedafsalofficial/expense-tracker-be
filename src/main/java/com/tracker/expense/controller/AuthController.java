package com.tracker.expense.controller;

import com.tracker.expense.dto.ApiResponse;
import com.tracker.expense.dto.auth.LoginRequest;
import com.tracker.expense.dto.auth.RegisterRequest;
import com.tracker.expense.dto.auth.AuthResponse;
import com.tracker.expense.model.auth.User;
import com.tracker.expense.security.UserPrincipal;
import com.tracker.expense.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        User registeredUser = authService.register(request);

        AuthResponse authResponse = AuthResponse.builder()
                .id(registeredUser.getId())
                .name(registeredUser.getName())
                .email(registeredUser.getEmail())
                .role(registeredUser.getRole())
                .build();

        return ResponseEntity.ok().body(
                ApiResponse.of("User registered successfully", authResponse));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response) {
        AuthResponse authResponse = authService.login(request, response);

        return ResponseEntity.ok().body(
                ApiResponse.of("User logged in successfully", authResponse));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<?>> logout(Authentication authentication, HttpServletResponse response) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        authService.logout(principal.getUser(), response);
        return ResponseEntity.ok(ApiResponse.of("Logged out successfully", null));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<?>> refreshToken(
            @CookieValue(name = "REFRESH_TOKEN", required = false) String refreshToken,
            HttpServletResponse response) {
        authService.refreshAccessToken(refreshToken, response);
        return ResponseEntity.ok(ApiResponse.of("Access token refreshed successfully", null));
    }
}
