package io.dami.market.config;

import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.kafka.ConfluentKafkaContainer;
import org.testcontainers.utility.DockerImageName;

@Configuration
public class TestcontainersConfiguration {

  public static final MySQLContainer<?> MYSQL_CONTAINER;

  private static final int REDIS_PORT = 6379;
  private static final String REDIS_IMAGE = "redis:latest";
  private static final String REDIS_TEST_PASSWORD = "testpassword";

  public static final GenericContainer REDIS;

  static {
    MYSQL_CONTAINER = new MySQLContainer<>(DockerImageName.parse("mysql:8.0"))
        .withDatabaseName("market")
        .withUsername("test")
        .withPassword("test");
    MYSQL_CONTAINER.start();

    System.setProperty("spring.datasource.url",
        MYSQL_CONTAINER.getJdbcUrl() + "?characterEncoding=UTF-8&serverTimezone=UTC");
    System.setProperty("spring.datasource.username", MYSQL_CONTAINER.getUsername());
    System.setProperty("spring.datasource.password", MYSQL_CONTAINER.getPassword());

    REDIS = new GenericContainer(DockerImageName.parse(REDIS_IMAGE))
        .withExposedPorts(REDIS_PORT)
        .withCommand("redis-server", "--requirepass", REDIS_TEST_PASSWORD)
        .waitingFor(Wait.forListeningPort());

    REDIS.start();
    System.setProperty("spring.data.redis.host", REDIS.getHost());
    System.setProperty("spring.data.redis.port", String.valueOf(REDIS.getFirstMappedPort()));
    System.setProperty("spring.data.redis.password", REDIS_TEST_PASSWORD);
  }

  @PreDestroy
  public void preDestroy() {
    if (MYSQL_CONTAINER.isRunning()) {
      MYSQL_CONTAINER.stop();
    }
    if (REDIS.isRunning()) {
      REDIS.stop();
    }
  }
}
