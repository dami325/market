package io.dami.market.domain.order;

import io.dami.market.interfaces.order.OrderRequest;

import java.util.List;

public record OrderCommand() {

    public record DoOrder(Long userId, Long couponId, List<Product> products) {}

    public record Product(Long productId, int quantity) {
        public Product(OrderRequest.CreateOrder.Product product) {
            this(product.productId(), product.quantity());
        }
    }
}
