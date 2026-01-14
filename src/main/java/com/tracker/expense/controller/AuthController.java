package com.tracker.expense.controller;

import com.tracker.expense.dto.ApiResponse;
import com.tracker.expense.dto.auth.LoginRequest;
import com.tracker.expense.dto.auth.RegisterRequest;
import com.tracker.expense.dto.auth.AuthResponse;
import com.tracker.expense.model.User;
import com.tracker.expense.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        User loggedInUser = authService.login(request);
        String jwtToken = authService.generateJwt(request.email());

        ResponseCookie cookie = ResponseCookie.from("ACCESS_TOKEN", jwtToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(24 * 60 * 60)  // 1 day
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        AuthResponse authResponse = AuthResponse.builder()
                .id(loggedInUser.getId())
                .name(loggedInUser.getName())
                .email(loggedInUser.getEmail())
                .role(loggedInUser.getRole())
                .build();

        return ResponseEntity.ok().body(
                ApiResponse.of("User logged in successfully", authResponse));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<?>> logout(HttpServletResponse response) {
        ResponseCookie deleteCookie = ResponseCookie.from("ACCESS_TOKEN", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString());

        return ResponseEntity.ok(ApiResponse.of("Logged out successfully", null));
    }
}
