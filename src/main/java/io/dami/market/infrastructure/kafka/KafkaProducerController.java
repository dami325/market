package io.dami.market.infrastructure.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/kafka")
public class KafkaProducerController {

  private final KafkaProducerService kafkaProducerService;

  public KafkaProducerController(KafkaProducerService kafkaProducerService) {
    this.kafkaProducerService = kafkaProducerService;
  }

  /**
   * 지정 토픽으로 메시지를 전송합니다.
   */
  @PostMapping("/messages")
  public ResponseEntity<String> sendMessage(@RequestBody String message) {
    kafkaProducerService.sendMessage(message);
    return ResponseEntity.ok("메시지 전송 완료");
  }

  /**
   * 지정 토픽으로 키와 함께 메시지를 전송합니다
   */
  @PostMapping("/messages/withKey")
  public ResponseEntity<String> sendMessageWithKey(@RequestParam String key,
      @RequestBody String message) {
    kafkaProducerService.sendMessageWithKey(key, message);
    return ResponseEntity.ok("키와 함께 메시지 전송 완료");
  }

  /**
   * 지정 토픽 내의 특정 파티션으로 메시지를 전송합니다.
   *
   * @param partition 파티션 명
   * @param message   메시지
   * @return
   */
  @PostMapping("/messages/toPartition/{partition}")
  public ResponseEntity<String> sendMessageToPartition(@PathVariable int partition,
      @RequestBody String message) {
    kafkaProducerService.sendMessageToPartition(message, partition);
    return ResponseEntity.ok("특정 파티션으로 메시지 전송 완료");
  }

  /**
   * 지정 토픽으로 메시지를 비동기 처리 수행합니다.
   *
   * @param message
   * @return
   */
  @PostMapping("/messages/async")
  public ResponseEntity<String> sendMessageWithCallback(@RequestBody String message) {
    kafkaProducerService.sendMessageWithCallback(message);
    return ResponseEntity.ok("비동기 메시지 전송 요청 완료");
  }
}
