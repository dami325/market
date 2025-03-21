package io.dami.market.infrastructure.kafka;

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
public class KafkaProducerService {

  private final KafkaTemplate<String, String> kafkaTemplate;
  private final String TOPIC_NAME = "example-topic";

  /**
   * 기본적인 메시지 전송
   */
  public void sendMessage(String message) {
    kafkaTemplate.send(TOPIC_NAME, message);
  }

  /**
   * 키와 함께 메시지 전송
   */
  public void sendMessageWithKey(String key, String message) {
    kafkaTemplate.send(TOPIC_NAME, key, message);
  }

  /**
   * 특정 파티션으로 메시지 전송
   */
  public void sendMessageToPartition(String message, int partition) {
    kafkaTemplate.send(TOPIC_NAME, partition, null, message);
  }

  /**
   * 비동기 전송 결과 처리
   */
  public void sendMessageWithCallback(String message) {
    kafkaTemplate.send(TOPIC_NAME, message)
        .whenComplete((result, ex) -> {
          if (ex == null) {
            log.info("Success: {} ", result.getRecordMetadata());
          } else {
            log.info("Failed: {}", ex.getMessage());
          }
        });
  }
}
