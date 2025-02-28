package io.dami.market.infra.dataplatform;

import io.dami.market.domain.payment.event.PayCompleteEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 데이터 플랫폼
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataPlatformServiceServiceImpl implements DataPlatformService {

  // 주문 정보 전송
  @Override
  public void sendOrderPaymentData(PayCompleteEvent event) {
    log.info("Publishing to Mock Data Platform. Order ID: {}, Payment ID: {}", event.orderId(),
        event.paymentId());
  }
}
