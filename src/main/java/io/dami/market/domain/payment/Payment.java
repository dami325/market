package io.dami.market.domain.payment;

import io.dami.market.domain.Auditor;
import io.dami.market.domain.order.Order;
import io.dami.market.domain.order.OrderDetail;
import io.dami.market.domain.payment.exception.PointNotEnoughException;
import io.dami.market.domain.user.User;
import io.dami.market.domain.user.UserPoint;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Getter
@Table(name = "payment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends Auditor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("결제 고유 ID")
    @Column(name = "id")
    private Long id;

    @Comment("주문 ID (외래 키)")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Comment("결제 상태 (예: 성공, 실패)")
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatue paymentStatus;

    @Comment("결제 금액")
    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Comment("결제 실패 원인")
    @Column(name = "failure_reason", length = 255)
    private PaymentFailureReason failureReason;

    public void pay() {
        User user = this.order.getUser();
        UserPoint userPoint = user.getUserPoint();
        if (this.amount.compareTo(userPoint.getBalance()) > 0) {
            this.paymentStatus = PaymentStatue.FAIL;
            this.order.updateOrderStatus(Order.OrderStatus.PENDING_PAYMENT);
            this.failureReason = PaymentFailureReason.NOT_ENOUGH_POINTS;
            return;
        }
        else if(order.isInvalidProductQuantity()){
            this.paymentStatus = PaymentStatue.FAIL;
            this.order.updateOrderStatus(Order.OrderStatus.OUT_OF_STOCK);
            this.failureReason = PaymentFailureReason.NOT_ENOUGH_PRODUCT_QUANTITY;
            return;
        }

        order.getOrderDetails().forEach(OrderDetail::productStockSubtract); // 재고 차감
        userPoint.usePoint(this.amount);
        this.paymentStatus = PaymentStatue.SUCCESS;
        this.order.updateOrderStatus(Order.OrderStatus.PAYMENT_SUCCESS);
    }

    public enum PaymentStatue {
        SUCCESS,
        FAIL
    }

    public Payment(Order order) {
        if (order.getStatus() == Order.OrderStatus.PAYMENT_SUCCESS) {
            throw new IllegalArgumentException("이미 결제가 완료된 주문");
        }
        this.amount = order.getTotalPrice();
        this.order = order;
    }
}
