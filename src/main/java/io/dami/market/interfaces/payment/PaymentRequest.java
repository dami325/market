package io.dami.market.interfaces.payment;

public record PaymentRequest() {

    public record Pay(
            Long userId,
            Long orderId
    ) {}
}
