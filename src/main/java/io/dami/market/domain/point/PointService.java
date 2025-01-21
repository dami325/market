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

    private final PointRepository pointRepository;

    @Transactional(readOnly = true)
    public BigDecimal getBalance(Long userId) {
        return pointRepository.getPointByUserId(userId)
                .getTotalPoint();
    }

    @Transactional
    public void chargePoint(Long userId, BigDecimal amount) {
        pointRepository.getPointByUserId(userId)
                .charge(amount);
    }

    @Transactional
    public void usePoints(Long userId, BigDecimal totalAmount) {
        Point point = pointRepository.getPointByUserIdWithLock(userId);
        point.subtract(totalAmount);
    }
}
