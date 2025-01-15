package io.dami.market.domain.order;

import io.dami.market.interfaces.order.OrderRequest;

import java.util.List;

public record OrderCommand() {

    public record order(
            Long userId,
            Long userCouponId,
            List<OrderDetails> orderDetails
    ) {

    }

    public record OrderDetails(
            Long productId,
            int quantity
    ) {
        public OrderDetails(
                OrderRequest.CreateOrder.OrderDetails orderDetails
        ) {
            this(orderDetails.productId(), orderDetails.quantity());
        }
    }
}
