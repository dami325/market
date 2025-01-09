package io.dami.market.application.order;

import io.dami.market.application.payment.PaymentService;
import io.dami.market.domain.order.DataPlatform;
import io.dami.market.domain.order.Order;
import io.dami.market.domain.order.OrderCommand;
import io.dami.market.domain.payment.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderFacade {

    private final OrderService orderService;
    private final PaymentService paymentService;
    private final DataPlatform dataPlatform; // 데이터 플랫폼 전송 역할을 가진 클래스

    public void createOrder(OrderCommand.DoOrder command) {
        Order order = orderService.order(command);
        Payment payment = paymentService.pay(order.getId());

        // 외부 플랫폼으로 주문정보 전송
        dataPlatform.publish(order, payment);
    }
}
