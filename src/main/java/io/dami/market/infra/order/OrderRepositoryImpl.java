package io.dami.market.infra.order;

import io.dami.market.domain.order.Order;
import io.dami.market.domain.order.OrderDetail;
import io.dami.market.domain.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;

    @Override
    public Order save(Order order) {
        return null;
    }

    @Override
    public Order save(OrderDetail order) {
        return null;
    }

    @Override
    public Order getOrderWithLock(Long orderId) {
        return orderJpaRepository.getOrderWithLock(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문번호"));
    }

}
