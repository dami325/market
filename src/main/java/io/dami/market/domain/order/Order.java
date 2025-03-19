package io.dami.market.domain.order;

import io.dami.market.domain.Auditor;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Comment;

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

  @Column(name = "user_id", nullable = false)
  @Comment("주문 요청자")
  private Long userId;

  @Column(name = "issued_coupon_id")
  @Comment("주문 시 사용한 유저 쿠폰")
  private Long issuedCouponId;

  @Enumerated(EnumType.STRING)
  @Comment("주문 상태 (예: 대기, 완료, 실패)")
  @Column(name = "status", nullable = false)
  private OrderStatus status;

  @Comment("총 결제 금액")
  @Builder.Default
  @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
  private BigDecimal totalAmount = BigDecimal.ZERO;

  @Comment("할인 금액")
  @Builder.Default
  @Column(name = "discount_amount", nullable = false, precision = 10, scale = 2)
  private BigDecimal discountAmount = BigDecimal.ZERO;

  @Builder.Default
  @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST,
      CascadeType.MERGE})
  private Set<OrderDetail> orderDetails = new HashSet<>();

  public static Order createOrderForm(Long userId, Long issuedCouponId) {
    return Order.builder()
        .userId(userId)
        .issuedCouponId(issuedCouponId)
        .status(OrderStatus.ORDER_COMPLETE)
        .build();
  }

  public void addOrderDetail(OrderDetail orderDetail) {
    this.totalAmount = this.totalAmount.add(orderDetail.getTotalPrice());
    this.orderDetails.add(orderDetail);
  }

  public Map<Long, Integer> getProductQuantityMap() {
    return this.orderDetails.stream()
        .collect(Collectors.toMap(
            OrderDetail::getProductId,  // 상품 ID를 키로
            OrderDetail::getQuantity,   // 수량을 값으로
            Integer::sum                // 같은 상품 ID가 있을 경우 수량 합산
        ));
  }

  public void discountApply(BigDecimal discountAmount) {
    this.discountAmount = discountAmount;

    // 총 주문 금액보다 할인 금액이 큰 경우 처리
    if (this.discountAmount.compareTo(this.totalAmount) > 0) {
      this.discountAmount = this.totalAmount; // 할인 금액을 주문 총액으로 조정
    }
    this.totalAmount = this.totalAmount.subtract(this.discountAmount);
  }

  @Getter
  @RequiredArgsConstructor
  public enum OrderStatus {
    ORDER_COMPLETE("주문 완료"),
    ORDER_CANCELLED("주문 취소");

    private final String description;
  }


}

