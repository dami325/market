package io.dami.market.utils.fixture;

import io.dami.market.domain.point.Point;
import java.math.BigDecimal;

public record PointFixture() {

  public static Point point(Long userId, int point) {
    return Point.builder()
        .userId(userId)
        .totalPoint(BigDecimal.valueOf(point))
        .build();
  }
}
