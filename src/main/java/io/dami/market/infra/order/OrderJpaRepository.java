package io.dami.market.infra.order;

import io.dami.market.domain.order.Order;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select orders 
            from Order orders
            where orders.id = :orderId
            """)
    Optional<Order> getOrderWithLock(Long orderId);
}
