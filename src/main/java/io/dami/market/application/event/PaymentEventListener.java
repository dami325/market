package io.dami.market.application.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dami.market.domain.outbox.PaymentOutbox;
import io.dami.market.domain.outbox.PaymentOutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventListener {

  private final PaymentEventPublisher paymentEventPublisher;
  private final PaymentOutboxRepository paymentOutboxRepository;
  private final ObjectMapper objectMapper;

  @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
  public void saveOutBox(PayCompleteEvent event) {
    try {
      paymentOutboxRepository.savePaymentOutbox(
          PaymentOutbox.create(objectMapper.writeValueAsString(event)));
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void sendOrderPaymentData(PayCompleteEvent event) {
    paymentEventPublisher.sendMessage(event);
  }

}
