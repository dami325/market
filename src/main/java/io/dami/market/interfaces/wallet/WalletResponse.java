package io.dami.market.interfaces.wallet;

import java.math.BigDecimal;

public record WalletResponse() {


    public record WalletDetails(
            Long walletId,
            BigDecimal balance
    ) {
    }
}
