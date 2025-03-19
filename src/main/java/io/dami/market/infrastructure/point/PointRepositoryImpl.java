package io.dami.market.infrastructure.point;

import io.dami.market.domain.point.Point;
import io.dami.market.domain.point.PointRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PointRepositoryImpl implements PointRepository {

  private final PointJpaRepository pointJpaRepository;

  @Override
  public Point getPointByUserId(Long userId) {
    return pointJpaRepository.findByUserId(userId)
        .orElseThrow(EntityNotFoundException::new);
  }

  @Override
  public Point getPointByUserIdWithLock(Long userId) {
    return pointJpaRepository.findByUserIdWithLock(userId)
        .orElseThrow(EntityNotFoundException::new);
  }

  @Override
  public Point save(Point point) {
    return pointJpaRepository.save(point);
  }

}
