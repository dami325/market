package io.dami.market.domain.point;

import io.dami.market.infrastructure.redis.redisson.DistributedLock;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PointService {

  private final PointRepository pointRepository;

  @Transactional(readOnly = true)
  public BigDecimal getBalance(Long userId) {
    return pointRepository.getPointByUserId(userId)
        .getTotalPoint();
  }

  @DistributedLock(key = "'POINT_' + #userId")
  @Transactional
  public void chargePoint(Long userId, BigDecimal amount) {
    pointRepository.getPointByUserId(userId)
        .charge(amount);
  }

  @Transactional
  public void usePoints(Long userId, BigDecimal totalAmount) {
    pointRepository.getPointByUserIdWithLock(userId)
        .subtract(totalAmount);
  }
}
