package io.dami.market.domain.order;

import io.dami.market.domain.Auditor;
import io.dami.market.domain.coupon.Coupon;
import io.dami.market.domain.product.Product;
import io.dami.market.domain.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;

@Entity
@Getter
@Builder
@AllArgsConstructor
@Table(name = "tb_order")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends Auditor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("주문 고유 ID")
    @Column(name = "id")
    private Long id;

    @Comment("사용자 ID (외래 키)")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Comment("상품 ID (외래 키)")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Comment("쿠폰 ID (외래 키, 선택)")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @Comment("주문 수량")
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Comment("총 주문 금액")
    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Comment("주문 상태 (예: 대기, 완료, 실패)")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @Getter
    @RequiredArgsConstructor
    public enum OrderStatus {
        OUT_OF_STOCK("재고 소진 대기"),
        PENDING_PAYMENT("결제 대기"),
        PAYMENT_SUCCESS("결제 완료"),
        ORDER_CANCELLED("주문 취소");

        private final String description;
    }
}
