package io.dami.market.interfaces.order;

import io.dami.market.domain.order.OrderCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrderRequest() {

    public record CreateOrder(
            @Schema(example = "5")
            @NotNull
            Long userId,
            @NotEmpty
            List<Product> products,
            @Schema(example = "3")
            Long couponId
    ) {
        public record Product(
                @Schema(example = "5")
                Long productId,
                @Schema(example = "3")
                int quantity
        ) {
        }

        public OrderCommand.DoOrder toCommand() {
            return new OrderCommand.DoOrder(
                    userId,
                    couponId,
                    products.stream()
                            .map(OrderCommand.Product::new)
                            .toList()
            );
        }
    }
}
