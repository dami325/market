package io.dami.market.config;

import io.dami.market.interfaces.interceptor.UserValidationInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

	private final UserValidationInterceptor userValidationInterceptor;
	private static final String[] USER_VALIDATION_URL = {
			"/api/v1/coupons/**",
	};
	private static final String[] WHITE_LIST = {
			""
	};

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(userValidationInterceptor)
				.addPathPatterns(USER_VALIDATION_URL) // 검증을 적용할 URL 패턴
				.excludePathPatterns(WHITE_LIST); // 예외 처리할 URL 패턴
	}
}
