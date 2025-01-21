package io.dami.market.domain.point;

public interface PointRepository {
    Point getPointByUserId(Long userId);

    Point getPointByUserIdWithLock(Long userId);

    Point save(Point point);
}
