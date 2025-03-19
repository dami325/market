package io.dami.market.infrastructure.dataplatform;

import io.dami.market.application.payment.event.PayCompleteEvent;
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

  @Override
  public void sendOrderPaymentData(PayCompleteEvent event) {
    log.info("Publishing to Mock Data Platform. Order ID: {}, Payment ID: {}", event.orderId(),
        event.paymentId());
  }
}
