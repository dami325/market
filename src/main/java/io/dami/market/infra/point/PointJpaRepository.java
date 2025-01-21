package io.dami.market.infra.point;

import io.dami.market.domain.point.Point;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PointJpaRepository extends JpaRepository<Point, Long> {

    Optional<Point> findByUserId(Long userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = """
    SELECT point
    FROM Point point
    WHERE point.userId = :userId
    """)
    Optional<Point> findByUserIdWithLock(Long userId);
}
