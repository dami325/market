package io.dami.market.interfaces.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dami.market.application.payment.event.PayCompleteEvent;
import io.dami.market.domain.payment.outbox.PaymentOutbox;
import io.dami.market.domain.payment.outbox.PaymentOutbox.PaymentOutboxStatus;
import io.dami.market.domain.payment.outbox.PaymentOutboxService;
import io.dami.market.infrastructure.dataplatform.DataPlatformService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentConsumer {

  private final String TOPIC_NAME = "order-payment";
  private final ObjectMapper objectMapper;
  private final PaymentOutboxService paymentOutboxService;
  private final DataPlatformService dataPlatformService;

  @KafkaListener(topics = TOPIC_NAME, groupId = "group-1")
  @Transactional
  public void sendDataPlatform(String payload) throws JsonProcessingException {
    log.info("메시지 수신: {}", payload);

    PaymentOutbox paymentOutbox = paymentOutboxService.getByPayload(payload);

    if (paymentOutbox.getStatus() == PaymentOutbox.PaymentOutboxStatus.SUCCESS) {
      return;
    }

    PayCompleteEvent payCompleteEvent = objectMapper.readValue(payload, PayCompleteEvent.class);
    paymentOutbox.changeStatus(PaymentOutboxStatus.FAIL);
    dataPlatformService.sendOrderPaymentData(payCompleteEvent);
    paymentOutbox.changeStatus(PaymentOutboxStatus.SUCCESS);
  }

}
