package com.tracker.expense.controller;

import com.tracker.expense.dto.ApiResponse;
import com.tracker.expense.dto.RegisterRequest;
import com.tracker.expense.dto.UserResponse;
import com.tracker.expense.model.User;
import com.tracker.expense.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<ApiResponse<UserResponse>> register(@Valid @RequestBody RegisterRequest request) {
        User registeredUser = authService.register(request);

        UserResponse userResponse = UserResponse.builder()
                .id(registeredUser.getId())
                .name(registeredUser.getName())
                .email(registeredUser.getEmail())
                .build();

        return ResponseEntity.ok().body(
                ApiResponse.of("User registered successfully", userResponse));
    }
}
