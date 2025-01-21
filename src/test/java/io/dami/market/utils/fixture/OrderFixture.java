package io.dami.market.utils.fixture;

import io.dami.market.domain.order.Order;
import io.dami.market.domain.order.OrderDetail;
import io.dami.market.domain.product.Product;

public record OrderFixture() {

    public static Order order(Long userId) {
        return Order.builder()
                .userId(userId)
                .status(Order.OrderStatus.ORDER_COMPLETE)
                .build();
    }

    public static Order order(Long userId, int quantity, Product... products) {
        Order order = Order.builder()
                .userId(userId)
                .status(Order.OrderStatus.ORDER_COMPLETE)
                .build();
        for (Product product : products) {
            OrderDetail orderDetail = OrderDetail.createOrderDetail(order, product.getId(), quantity, product.getTotalPrice(quantity));
            order.addOrderDetail(orderDetail);
        }
        return order;
    }
}
