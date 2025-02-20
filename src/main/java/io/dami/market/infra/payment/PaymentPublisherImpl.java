package io.dami.market.infra.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dami.market.domain.payment.event.PayCompleteEvent;
import io.dami.market.domain.payment.event.PaymentEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * kafkaTemplate 활용한 Topic 내에 메시지를 전송하는 다양한 방법
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentPublisherImpl implements PaymentEventPublisher {

  private final KafkaTemplate<String, String> kafkaTemplate;
  private final String TOPIC_NAME = "order-payment";
  private final ObjectMapper objectMapper;

  @Override
  public void sendMessage(PayCompleteEvent event) {
    try {
      kafkaTemplate.send(TOPIC_NAME, objectMapper.writeValueAsString(event));
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
