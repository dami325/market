package io.dami.market.infra.order;

import io.dami.market.domain.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {

    @Query("""
            select orders 
            from Order orders
            where orders.id = :orderId
            and orders.status = 'ORDER_COMPLETE'
            """)
    Optional<Order> getCompleteOrder(Long orderId);
}
