package io.dami.market.domain.point;

import io.dami.market.domain.user.User;
import io.dami.market.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PointService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public BigDecimal getBalance(Long userId) {
        return userRepository.getUser(userId)
                .getUserPoint()
                .getBalance();
    }

    @Transactional
    public void chargePoint(Long userId, BigDecimal amount) {
        userRepository.getUser(userId)
                .chargePoint(amount);
    }

    @Transactional
    public void usePoints(Long userId, BigDecimal totalAmount) {
        User user = userRepository.getUserWithLock(userId);
        user.usePoint(totalAmount);
    }
}
