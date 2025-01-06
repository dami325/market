package io.dami.market.interfaces.point;

import java.math.BigDecimal;

public record PointRequest() {

    public record ChargeWallet(
        Long userId,
        BigDecimal amount
    ) {
    }
}
