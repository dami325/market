package io.dami.market.application.event;

public interface PaymentEventPublisher {

  void sendMessage(PayCompleteEvent event);
}
