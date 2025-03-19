package io.dami.market.application.payment.event;

public record PayCompleteEvent(
    Long orderId,
    Long paymentId
) {

}
