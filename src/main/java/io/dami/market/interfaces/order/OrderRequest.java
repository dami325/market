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
            List<OrderDetails> products
    ) {
        public record OrderDetails(
                @Schema(example = "5")
                Long productId,
                @Schema(example = "3")
                int quantity
        ) {
        }

        public List<OrderCommand.OrderDetails> toCommand() {
            return products.stream()
                    .map(OrderCommand.OrderDetails::new)
                    .toList();
        }
    }
}
