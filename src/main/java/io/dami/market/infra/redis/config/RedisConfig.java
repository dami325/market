package io.dami.market.infra.redis.config;

import lombok.RequiredArgsConstructor;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

  private final RedisProperties redisProperties;
  private static final String REDISSON_HOST_PREFIX = "redis://";

  /**
   * Redis 와의 연결을 위한 'Connection'을 생성합니다.
   *
   * @return
   */
  @Bean
  public RedisConnectionFactory redisConnectionFactory() {
    RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
    configuration.setHostName(redisProperties.getHost());
    configuration.setPort(redisProperties.getPort());
    configuration.setPassword(redisProperties.getPassword()); // 비밀번호 설정
    return new LettuceConnectionFactory(configuration);
  }

  /**
   * Redis 데이터 처리를 위한 템플릿을 구성합니다. 해당 구성된 RedisTemplate을 통해서 데이터 통신으로 처리되는 대한 직렬화를 수행합니다.
   *
   * @return
   */
  @Bean
  public RedisTemplate<String, Object> redisTemplate() {
    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

    // Redis를 연결합니다.
    redisTemplate.setConnectionFactory(redisConnectionFactory());

    // Key-Value 형태로 직렬화를 수행합니다.
    redisTemplate.setKeySerializer(new StringRedisSerializer());
    redisTemplate.setValueSerializer(new StringRedisSerializer());

    // Hash Key-Value 형태로 직렬화를 수행합니다.
    redisTemplate.setHashKeySerializer(new StringRedisSerializer());
    redisTemplate.setHashValueSerializer(new StringRedisSerializer());

    // 기본적으로 직렬화를 수행합니다.
    redisTemplate.setDefaultSerializer(new StringRedisSerializer());

    // redisTemplate 사용 시 트랜잭션 관리 여부
//        redisTemplate.setEnableTransactionSupport(true);
    return redisTemplate;
  }

  /**
   * Redisson 분산락용
   */
  @Bean
  public RedissonClient redissonClient() {
    Config config = new Config();
    config.useSingleServer()
        .setAddress(
            REDISSON_HOST_PREFIX + redisProperties.getHost() + ":" + redisProperties.getPort())
        .setPassword(redisProperties.getPassword());

    return Redisson.create(config);
  }

}
