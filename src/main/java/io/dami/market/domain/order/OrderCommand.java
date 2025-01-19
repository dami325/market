package io.dami.market.domain.order;

import io.dami.market.interfaces.order.OrderRequest;

public record OrderCommand() {

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
