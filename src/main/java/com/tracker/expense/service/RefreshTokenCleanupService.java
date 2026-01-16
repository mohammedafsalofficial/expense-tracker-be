package com.tracker.expense.service;

import com.tracker.expense.repository.auth.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefreshTokenCleanupService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteByUserId(Long userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }
}
