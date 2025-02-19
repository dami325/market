package io.dami.market.application.payment;

public record PayCompleteEvent(
    Long orderId,
    Long paymentId
) {

}
