package io.dami.market.infra.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("E 커머스 서비스 프로젝트 API Document")
                .version("v1.0.0")
                .description("E 커머스 서비스 API 명세서입니다.");
        return new OpenAPI().info(info);
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("market")
                .pathsToMatch("/**")
                .build();
    }
}
