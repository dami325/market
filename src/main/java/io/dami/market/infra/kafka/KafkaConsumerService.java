package io.dami.market.infra.kafka;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * Kafka Topic을 구독하여 메시지를 수신하는 서비스
 */
@Slf4j
@Service
public class KafkaConsumerService {

  /**
   * 기본적인 토픽 리스닝
   *
   * @param message 수신 메시지
   */
  @KafkaListener(topics = "example-topic", groupId = "group-1")
  public void listen(String message) {
    log.info("기본 메시지 수신: {}", message);
  }

  /**
   * 동시성과 자동시작 설정
   *
   * @param message 수신 메시지
   */
  @KafkaListener(
      topics = "high-volume-topic",
      groupId = "group-2",
      concurrency = "3",
      autoStartup = "true"
  )
  public void listenWithConcurrency(String message) {
    log.info("동시처리 메시지 수신: {}", message);
  }

  /**
   * 배치 처리와 에러 핸들러 설정
   *
   * @param messages 수신 메시지
   */
  @KafkaListener(
      topics = "batch-topic",
      groupId = "group-3",
      batch = "true",
      errorHandler = "customErrorHandler"
  )
  public void listenBatch(List<String> messages) {
    log.info("배치 메시지 수신: {}", messages.size() + "개");
  }

  /**
   * 토픽 패턴 사용
   *
   * @param message 수신 메시지
   */
  @KafkaListener(
      topicPattern = "test.*",
      groupId = "group-4",
      clientIdPrefix = "pattern-client"
  )
  public void listenToPattern(String message) {
    log.info("패턴 토픽 메시지 수신: {}", message);
  }
}
