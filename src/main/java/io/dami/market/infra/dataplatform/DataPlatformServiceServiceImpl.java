package io.dami.market.infra.dataplatform;

import io.dami.market.application.payment.PayCompleteEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * 데이터 플랫폼
 */
@Slf4j
@Component
public class DataPlatformServiceServiceImpl implements DataPlatformService {

  // 주문 정보 전송
  @Override
  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void sendOrderPaymentData(PayCompleteEvent event) {
    log.info("Publishing to Mock Data Platform. Order ID: {}, Payment ID: {}", event.orderId(),
        event.paymentId());
  }
}
