package io.dami.market.interfaces.order;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrderRequest() {

    public record OrderCreate(
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
    }
}
