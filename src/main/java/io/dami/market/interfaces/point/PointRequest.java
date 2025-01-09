package io.dami.market.interfaces.point;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record PointRequest() {

    public record ChargeWallet(
            @Schema(example = "5")
            Long userId,
            @Schema(example = "100000")
            BigDecimal amount
    ) {
    }
}
