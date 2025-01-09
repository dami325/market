package io.dami.market.domain.order;

public interface OrderRepository {
    Order save(Order order);

    Order getOrderWithLock(Long orderId);
}
