package io.dami.market.utils.fixture;

import io.dami.market.domain.order.Order;
import io.dami.market.domain.user.User;

import java.math.BigDecimal;

public record OrderFixture() {

    public static Order order(User user){
        return Order.builder()
                .user(user)
                .build();
    }
}
