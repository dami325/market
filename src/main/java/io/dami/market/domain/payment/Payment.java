package io.dami.market.domain.payment;

import io.dami.market.domain.Auditor;
import io.dami.market.domain.order.Order;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;

@Entity
@Getter
@Builder
@AllArgsConstructor
@Table(name = "payment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends Auditor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("결제 고유 ID")
    @Column(name = "id")
    private Long id;

    @Comment("주문 ID (외래 키)")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Comment("결제 방법 (예: 포인트 결제)")
    @Column(name = "payment_method", nullable = false)
    private String paymentMethod;

    @Comment("결제 상태 (예: 성공, 실패)")
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatue paymentStatus;

    @Comment("결제 금액")
    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Comment("결제 실패 원인")
    @Column(name = "failure_reason", length = 255)
    private String failureReason;

    public enum PaymentStatue {
        SUCCESS,
        FAIL
    }
}
