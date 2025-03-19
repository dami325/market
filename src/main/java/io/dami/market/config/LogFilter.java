package io.dami.market.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

/**
 * 요청 응답 로그
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
public class LogFilter extends OncePerRequestFilter {

  /**
   * 민감 정보 필터 처리용
   */
  private static final String[] SENSITIVE_KEYS = {
      "userId",
      "password",
      "email"
  };

  /**
   * 정규표현식 캐싱해두고 쓰기
   */
  private static final Pattern SENSITIVE_PATTERN = Pattern.compile(
      "(?i)(\\b(" + String.join("|", SENSITIVE_KEYS) + ")\\b)=([^&]*)");
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    List<String> excludeUrls = List.of("/actuator", "/swagger");
    String requestURI = request.getRequestURI();
    return excludeUrls.stream().anyMatch(requestURI::startsWith);
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
    ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
    boolean exceptionOccurred = false;
    try {
      filterChain.doFilter(requestWrapper, responseWrapper);
    } catch (Exception e) {
      exceptionOccurred = true;
      logRequest(requestWrapper);
      throw e;
    } finally {
      if (!exceptionOccurred) { // 에러났을때 요청 로그 두번 남는거 방지용
        logRequest(requestWrapper);
      }
      logResponse(responseWrapper);
      responseWrapper.copyBodyToResponse();
    }
  }

  /**
   * request 로그
   */
  private void logRequest(ContentCachingRequestWrapper requestWrapper) {
    try {
      log.info("Request URL: {}", requestWrapper.getRequestURL());
      String parameters = maskSensitiveData(getParametersAsString(requestWrapper));
      log.info("Request Parameters: {}", parameters);

      String requestBody = new String(requestWrapper.getContentAsByteArray(),
          StandardCharsets.UTF_8);
      String maskedBody = maskSensitiveDataInJson(requestBody);
      log.info("Request Body: {}", maskedBody);
    } catch (Exception e) {
      log.warn("Failed to log request details", e);
    }
  }

  /**
   * response 로그
   */
  private void logResponse(ContentCachingResponseWrapper responseWrapper) {
    try {
      log.info("Response Status: {}", responseWrapper.getStatus());
      String responseBody = new String(responseWrapper.getContentAsByteArray(),
          StandardCharsets.UTF_8);
      log.info("Response Body: {}", responseBody);
    } catch (Exception e) {
      log.warn("Failed to log response details", e);
    }
  }

  /**
   * request parameter 로그 가공
   */
  private String getParametersAsString(ContentCachingRequestWrapper requestWrapper) {
    StringBuilder params = new StringBuilder();
    try {
      Map<String, String[]> parameterMap = requestWrapper.getParameterMap();
      parameterMap.forEach((key, values) -> {
        for (String value : values) {
          if (params.length() > 0) {
            params.append("&");
          }
          params.append(key).append("=").append(value);
        }
      });
    } catch (Exception e) {
      log.warn("Failed to retrieve request parameters", e);
    }
    return params.toString();
  }

  /**
   * 정규 표현식으로 파라미터 민감정보 처리
   */
  private String maskSensitiveData(String input) {
    if (input == null || input.isEmpty()) {
      return input;
    }
    return SENSITIVE_PATTERN.matcher(input).replaceAll("$1={masked}");
  }

  /**
   * requestBody 가공
   */
  private String maskSensitiveDataInJson(String json) {
    try {
      if (json == null || json.isEmpty()) {
        return json;
      }
      OBJECT_MAPPER.readTree(json);
      Map<String, Object> jsonMap = OBJECT_MAPPER.readValue(json, Map.class);

      maskSensitiveFields(jsonMap);

      return OBJECT_MAPPER.writeValueAsString(jsonMap);
    } catch (Exception e) {
      log.warn("Failed to mask JSON body");
      return "{masked}";
    }
  }

  /**
   * 민감정보 처리
   */
  private void maskSensitiveFields(Map<String, Object> jsonMap) {
    jsonMap.forEach((key, value) -> {
      if (value instanceof Map) {

        maskSensitiveFields((Map<String, Object>) value);
      } else if (value instanceof String && isSensitiveKey(key)) {
        jsonMap.put(key, "{masked}");
      }
    });
  }

  /**
   * 민감정보 필터
   */
  private boolean isSensitiveKey(String key) {
    for (String sensitiveKey : SENSITIVE_KEYS) {
      if (key.toLowerCase().contains(sensitiveKey.toLowerCase())) {
        return true;
      }
    }
    return false;
  }
}
