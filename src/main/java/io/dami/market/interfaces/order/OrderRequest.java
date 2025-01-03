package io.dami.market.interfaces.order;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrderRequest() {

    public record OrderCreate(
            @NotNull
            Long userId,
            @NotEmpty
            List<Product> products,
            Long couponId
    ) {
        public record Product (
            Long productId,
            int quantity
        ){}
    }
}
