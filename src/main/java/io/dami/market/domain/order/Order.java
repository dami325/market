package io.dami.market.domain.order;

import io.dami.market.domain.Auditor;
import io.dami.market.domain.coupon.Coupon;
import io.dami.market.domain.product.Product;
import io.dami.market.domain.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;
import java.util.HashSet;
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

    @Comment("주문 시 사용된 쿠폰 ID")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @Enumerated(EnumType.STRING)
    @Comment("주문 상태 (예: 대기, 완료, 실패)")
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @Comment("총 결제 금액")
    @Builder.Default
    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice = BigDecimal.ZERO;

    @Comment("할인 받은 금액")
    @Builder.Default
    @Column(name = "discount_amount", precision = 10, scale = 2)
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Builder.Default
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<OrderDetail> orderDetails = new HashSet<>();

    public void updateOrderStatus(OrderStatus orderStatus) {
        this.status = orderStatus;
    }

    public boolean isInvalidProductQuantity() {
        return this.orderDetails.stream()
                .anyMatch(OrderDetail::isInvalid) || this.orderDetails.isEmpty();
    }

    public void addOrderDetail(Product product,int quantity) {
        BigDecimal detailTotalPrice = product.getTotalPrice(quantity);
        OrderDetail orderDetail = OrderDetail.builder()
                .order(this)
                .product(product)
                .quantity(quantity)
                .totalPrice(detailTotalPrice)
                .build();
        this.totalPrice = this.totalPrice.add(detailTotalPrice);
        this.orderDetails.add(orderDetail);
    }

    public void disCount(BigDecimal discountAmount) {
        // 총 주문 금액보다 할인 금액이 큰 경우 처리
        if (discountAmount.compareTo(this.totalPrice) > 0) {
            discountAmount = this.totalPrice; // 할인 금액을 주문 총액으로 조정
        }
        this.totalPrice = this.totalPrice.subtract(discountAmount);
        this.discountAmount = discountAmount;
    }

    public List<Long> getOrderProductIds() {
        return this.getOrderDetails().stream()
                .map(OrderDetail::getProduct)
                .map(Product::getId)
                .toList();
    }

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

