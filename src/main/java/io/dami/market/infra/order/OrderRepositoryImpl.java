package io.dami.market.infra.order;

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
    public Order getOrder(Long orderId) {
        return orderJpaRepository.getOrder(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문번호"));
    }

}
