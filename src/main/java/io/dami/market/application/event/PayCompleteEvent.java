package io.dami.market.application.event;

public record PayCompleteEvent(
    Long orderId,
    Long paymentId
) {

}
