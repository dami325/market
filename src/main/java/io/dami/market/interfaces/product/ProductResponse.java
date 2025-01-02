package io.dami.market.interfaces.product;

import java.math.BigDecimal;

public record ProductResponse(

) {
    public record Top5ProductDetails(
            Long productId,
            String name,
            BigDecimal price,
            Integer stockQuantity,
            int rank,
            int total_quantity_sold
    ) {
    }

    public record ProductDetails(
            Long productId,
            String name,
            BigDecimal price,
            Integer stockQuantity
    ) {
    }
}
