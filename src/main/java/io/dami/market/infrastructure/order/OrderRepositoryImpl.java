package io.dami.market.infrastructure.order;

import io.dami.market.domain.order.Order;
import io.dami.market.domain.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;

    @Override
    public Order save(Order order) {
        return orderJpaRepository.save(order);
    }

    @Override
    public Order getCompleteOrder(Long orderId) {
        return orderJpaRepository.getCompleteOrder(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문번호"));
    }

}
