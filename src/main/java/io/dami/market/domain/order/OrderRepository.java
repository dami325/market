package io.dami.market.domain.order;

public interface OrderRepository {
    Order save(Order order);
    Order save(OrderDetail orderDetail);

    Order getOrderWithLock(Long orderId);
}
