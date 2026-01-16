package com.tracker.expense.service;

import com.tracker.expense.exception.RefreshTokenReuseException;
import com.tracker.expense.model.auth.RefreshToken;
import com.tracker.expense.model.auth.User;
import com.tracker.expense.repository.auth.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenCleanupService refreshTokenCleanupService;

    private static final long REFRESH_TOKEN_DAYS = 7;

    public RefreshToken createRefreshToken(User user) {
        RefreshToken refreshToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expiryDate(LocalDateTime.now().plusDays(REFRESH_TOKEN_DAYS))
                .revoked(false)
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken rotateRefreshToken(RefreshToken oldToken) {
        if (oldToken.isRevoked()) {
            refreshTokenCleanupService.deleteByUserId(oldToken.getUser().getId());
            throw new RefreshTokenReuseException();
        }

        if (oldToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(oldToken);
            throw new RuntimeException("Refresh token expired");
        }

        oldToken.setRevoked(true);
        refreshTokenRepository.save(oldToken);

        return createRefreshToken(oldToken.getUser());
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token expired");
        }
        return token;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteByUserId(Long userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }
}
