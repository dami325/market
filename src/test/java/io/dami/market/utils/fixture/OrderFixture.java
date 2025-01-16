package io.dami.market.utils.fixture;

import io.dami.market.domain.order.Order;
import io.dami.market.domain.product.Product;
import io.dami.market.domain.user.User;

import java.math.BigDecimal;

public record OrderFixture() {

    public static Order order(User user) {
        return Order.builder()
                .user(user)
                .build();
    }

    public static Order order(User user, int quantity, Product... products) {
        Order order = Order.builder()
                .user(user)
                .status(Order.OrderStatus.PENDING_PAYMENT)
                .build();
        for (Product product : products) {
            order.addOrderDetail(product, quantity);
        }
        return order;
    }
}
