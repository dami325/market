package io.dami.market.infra.kafka;

import io.dami.market.utils.IntegrationServiceTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class KafkaProducerServiceIntegrationTest extends IntegrationServiceTest {

  @Autowired
  private KafkaProducerService kafkaProducerService;

  @DisplayName("카프카 메시지 발행 테스트")
  @Test
  void sendMessage() {
    //given
    String message = "test";

    //when & then
    try {
      kafkaProducerService.sendMessage(message);
    } catch (Exception e) {
      Assertions.fail();
    }
  }
}
