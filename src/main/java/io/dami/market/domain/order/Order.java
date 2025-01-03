package io.dami.market.domain.order;

import io.dami.market.domain.Auditor;
import io.dami.market.domain.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Builder
@AllArgsConstructor
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends Auditor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("주문 이력 고유 ID")
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @Comment("사용자 ID (외래 키)")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Comment("주문 세부 항목 목록")
    private Set<OrderDetail> orderDetails;

    @Enumerated(EnumType.STRING)
    @Comment("주문 상태 (예: 대기, 완료, 실패)")
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @Comment("총 결제 금액")
    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Comment("할인 받은 금액")
    @Column(name = "discount_amount", precision = 10, scale = 2)
    private BigDecimal discountAmount;

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

