package io.dami.market.config;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.TopicConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.listener.KafkaListenerErrorHandler;
import org.springframework.util.backoff.BackOff;
import org.springframework.util.backoff.FixedBackOff;

/**
 * Kafka 연결 설정을 관리하는 관리파일.
 * <p>
 * 두 설정 방식이 공존할 때는 Java Configuration의 설정이 우선적으로 적용되며, 누락된 설정은 yaml 파일에서 보완하는 방식으로 동작합니다. 예를 들어서,
 * yaml 파일에서 bootstrap-servers=localhost:29092, group-id=group1로 설정하고 Java Configuration에서
 * bootstrap-servers=localhost:29093만 설정만 했을 경우 최종적으로 bootstrap-servers=localhost:29093(Java
 * Config), group-id=group1(yaml)이 됩니다.
 */
@Slf4j
@Configuration
public class KafkaConfig {

  // Kafka 연결 서버 주소
  @Value("${spring.kafka.bootstrap-servers}")
  private String bootstrapServers;

  /**
   * Producer 설정을 위한 Factory Bean - 메시지 생산자의 직렬화 설정 및 서버 연결 설정을 담당
   *
   * @return ProducerFactory<String, String>
   */
  @Bean
  public ProducerFactory<String, String> producerFactory() {
    Map<String, Object> configProps = new HashMap<>();
    configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

    configProps.put(ProducerConfig.ACKS_CONFIG, "all");
    // 카프카 프로듀서가 메시지 전송에 실패했을 때 재시도할 횟수 최대 3번까지 재시도한 뒤 그래도 실패하면 예외를 발생
    configProps.put(ProducerConfig.RETRIES_CONFIG, 3);

    return new DefaultKafkaProducerFactory<>(configProps);
  }

  /**
   * Consumer 설정을 위한 Factory Bean 메시지 소비자의 역직렬화 설정 및 그룹 ID, 오프셋 설정을 담당
   *
   * @return ConsumerFactory<String, String>
   */
  @Bean
  public ConsumerFactory<String, String> consumerFactory() {
    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "my-group");
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    return new DefaultKafkaConsumerFactory<>(props);
  }

  /**
   * - KafkaListenerContainerFactory의 구현체로, 메시지 리스너의 동시성을 지원합니다.
   * <p>
   * - 멀티스레드 환경에서 메시지를 효율적으로 처리할 수 있게 해 줍니다. 또한, 각 리스너 컨테이너에 대한 세부적인 설정이 가능합니다.
   */
  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
      ConsumerFactory<String, String> consumerFactory) {
    ConcurrentKafkaListenerContainerFactory<String, String> factory =
        new ConcurrentKafkaListenerContainerFactory<>();

    // Consumer 팩토리 설정
    factory.setConsumerFactory(consumerFactory);

    // 동시성 설정 - 3개의 스레드로 병렬 처리
    factory.setConcurrency(3);

    // 자동 시작 설정
    factory.setAutoStartup(true);

    // 배치 처리 모드 설정
    factory.setBatchListener(true);

    // 승인 모드 설정 - 배치 단위로 커밋
    factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.BATCH);

    // 에러 핸들러 설정
    factory.setCommonErrorHandler(defaultErrorHandler());

    return factory;
  }

  /**
   * Kafka 메시지를 전송하기 위한 템플릿 Bean 실제 애플리케이션에서 메시지 전송시 사용됨
   *
   * @return KafkaTemplate<String, String>
   */
  @Bean
  public KafkaTemplate<String, String> kafkaTemplate() {
    return new KafkaTemplate<>(producerFactory());
  }

  @Bean
  public NewTopic exampleTopic() {
    return TopicBuilder.name("example-topic")
        .partitions(3)                     // 파티션 수 설정
        .replicas(2)                       // 복제 팩터 설정
        .config(                           // 추가 설정
            TopicConfig.RETENTION_MS_CONFIG,
            String.valueOf(7 * 24 * 60 * 60 * 1000L)  // 7일
        )
        .build();
  }

  @Bean
  public NewTopic compactTopic() {
    return TopicBuilder.name("compact-topic")
        .partitions(1)
        .replicas(1)
        .compact()                         // 압축 정책 설정
        .build();
  }

  /**
   * DefaultErrorHandler 구성
   *
   * @return
   */
  @Bean
  public DefaultErrorHandler defaultErrorHandler() {
    // 재시도 정책 설정
    BackOff backOff = new FixedBackOff(1000L, 2);

    DefaultErrorHandler errorHandler = new DefaultErrorHandler((consumerRecord, exception) -> {
      // 최종 실패 시 처리할 로직
      log.debug("처리 실패한 레코드: {} exception : {} ", consumerRecord, exception);
    }, backOff);

    // 특정 예외는 재시도하지 않도록 설정
    errorHandler.addNotRetryableExceptions(IllegalArgumentException.class);
    return errorHandler;
  }

  /**
   * 각각의 리스너 별로 다른 에러 처리 로직을 적용할 수 있고, 메시지 처리 실패 시 대체 응답을 반환할 수 있습니다.
   *
   * @KafkaListener( errorHandler = "customErrorHandler" ) public void listen(String message){}
   */
  @Bean
  public KafkaListenerErrorHandler customErrorHandler() {
    return (message, exception) -> {
      log.error("메시지 처리 중 오류 발생: {}", exception.getMessage());
      log.error("문제가 발생한 메시지: {}", message.getPayload());
      // 예외 처리 로직 구현
      // 필요한 경우 다른 결과 반환 가능
      return "Error Handled";
    };
  }
}
