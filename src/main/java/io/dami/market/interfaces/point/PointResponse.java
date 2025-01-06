package io.dami.market.interfaces.point;

import java.math.BigDecimal;

public record PointResponse() {


    public record WalletDetails(
            Long walletId,
            BigDecimal balance
    ) {
    }
}
