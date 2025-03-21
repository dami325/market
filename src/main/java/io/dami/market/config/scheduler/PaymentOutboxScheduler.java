package io.dami.market.config.scheduler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dami.market.application.event.PayCompleteEvent;
import io.dami.market.application.event.PaymentEventPublisher;
import io.dami.market.domain.outbox.PaymentOutbox;
import io.dami.market.domain.outbox.PaymentOutboxRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentOutboxScheduler {

  private final PaymentOutboxRepository paymentOutboxRepository;
  private final PaymentEventPublisher paymentEventPublisher;
  private final ObjectMapper objectMapper;

  @Scheduled(fixedRate = 10000)
  public void paymentOutboxReissue() throws JsonProcessingException {
    List<PaymentOutbox> paymentOutboxes = paymentOutboxRepository.getReissueOutboxes();
    for (PaymentOutbox paymentOutbox : paymentOutboxes) {
      paymentEventPublisher.sendMessage(objectMapper.readValue(paymentOutbox.getPayload(),
          PayCompleteEvent.class));
    }
  }
}
