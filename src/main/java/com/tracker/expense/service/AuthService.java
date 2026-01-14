package com.tracker.expense.service;

import com.tracker.expense.dto.auth.LoginRequest;
import com.tracker.expense.dto.auth.RegisterRequest;
import com.tracker.expense.exception.EmailAlreadyExistsException;
import com.tracker.expense.exception.InvalidCredentialsException;
import com.tracker.expense.model.User;
import com.tracker.expense.repository.UserRepository;
import com.tracker.expense.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public User register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException(request.email());
        }

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .build();

        return userRepository.save(user);
    }

    public User login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()));
        } catch (AuthenticationException ex) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        return userRepository.findByEmail(request.email())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));
    }

    public String generateJwt(String email) {
        return jwtUtil.generateToken(email);
    }
}
