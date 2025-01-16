package io.dami.market.domain.payment;

import io.dami.market.domain.Auditor;
import io.dami.market.domain.order.Order;
import io.dami.market.domain.order.OrderAlreadyCanceledException;
import io.dami.market.domain.order.PaymentAlreadySuccessException;
import io.dami.market.domain.user.UserCoupon;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;

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
    @Column(name = "payment_status")
    private PaymentStatue paymentStatus;

    @Comment("최종 결제 금액")
    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Comment("할인 받은 금액")
    @Column(name = "discount_amount", precision = 10, scale = 2)
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @JoinColumn(name = "user_coupon_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private UserCoupon userCoupon;

    public enum PaymentStatue {
        SUCCESS,
        FAIL
    }

    public Payment(Order order, UserCoupon userCoupon) {
        this.orderValidate(order);
        this.amount = order.getTotalPrice();
        this.order = order;
        this.userCoupon = userCoupon;
        this.paymentStatus = PaymentStatue.FAIL;
    }

    public void pay() {
        this.discountAmount = userCoupon == null ? BigDecimal.ZERO : userCoupon.useCoupon();
        // 총 주문 금액보다 할인 금액이 큰 경우 처리
        if (this.discountAmount.compareTo(this.amount) > 0) {
            this.discountAmount = this.amount; // 할인 금액을 주문 총액으로 조정
        }
        this.amount = this.amount.subtract(this.discountAmount);

        this.paymentStatus = PaymentStatue.SUCCESS;
        this.order.updateOrderStatus(Order.OrderStatus.PAYMENT_SUCCESS);
    }

    private void orderValidate(Order order) {
        switch (order.getStatus()) {
            case PAYMENT_SUCCESS -> throw new PaymentAlreadySuccessException("이미 결제가 완료된 주문입니다.");
            case ORDER_CANCELLED -> throw new OrderAlreadyCanceledException("취소된 주문 입니다.");
        }
    }

}
