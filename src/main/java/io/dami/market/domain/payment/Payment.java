package io.dami.market.domain.payment;

import io.dami.market.domain.Auditor;
import io.dami.market.domain.order.Order;
import io.dami.market.domain.order.OrderAlreadyCanceledException;
import io.dami.market.domain.order.PaymentAlreadyInProcessException;
import io.dami.market.domain.order.PaymentAlreadySuccessException;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;

@Entity
@Getter
@Table(name = "payment")
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends Auditor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("결제 고유 ID")
    @Column(name = "id")
    private Long id;

    @Comment("주문 ID (외래 키)")
    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Comment("결제 상태 (예: 성공, 실패, 진행중)")
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatue paymentStatus;

    @Comment("최종 결제 금액")
    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    public static Payment createPaymentForm(Long orderId, BigDecimal totalAmount) {
        return Payment.builder()
                .orderId(orderId)
                .totalAmount(totalAmount)
                .paymentStatus(PaymentStatue.IN_PROGRESS)
                .build();
    }

    public void success() {
        this.paymentStatus = PaymentStatue.SUCCESS;
    }

    public enum PaymentStatue {
        SUCCESS,
        IN_PROGRESS,
        FAIL
    }

    public void paymentValidate() {
        switch (this.paymentStatus) {
            case PaymentStatue.SUCCESS -> throw new PaymentAlreadySuccessException("이미 결제가 완료된 주문입니다.");
            case PaymentStatue.IN_PROGRESS -> throw new PaymentAlreadyInProcessException("이미 진행중인 결제가 있습니다.");
        }
    }
}
