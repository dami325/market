package io.dami.market.interfaces.wallet;

import java.math.BigDecimal;

public record WalletRequest() {

    public record ChargeWallet(
        Long userId,
        BigDecimal amount
    ) {
    }
}
