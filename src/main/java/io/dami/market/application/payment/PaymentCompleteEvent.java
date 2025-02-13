package io.dami.market.application.payment;

public record PaymentCompleteEvent(
    Long orderId,
    Long paymentId
) {

}
