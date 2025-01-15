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
            List<OrderDetails> products,
            @Schema(example = "3")
            Long userCouponId
    ) {
        public record OrderDetails(
                @Schema(example = "5")
                Long productId,
                @Schema(example = "3")
                int quantity
        ) {
        }

        public OrderCommand.order toCommand() {
            return new OrderCommand.order(
                    userId,
                    userCouponId,
                    products.stream()
                            .map(OrderCommand.OrderDetails::new)
                            .toList()
            );
        }
    }
}
