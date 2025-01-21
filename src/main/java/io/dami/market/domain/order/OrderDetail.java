package io.dami.market.domain.order;

import io.dami.market.domain.Auditor;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;

@Entity
@Getter
@Builder
@AllArgsConstructor
@Table(name = "order_detail")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderDetail extends Auditor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("주문 상세 ID")
    @Column(name = "id")
    private Long id;

    @Comment("주문 ID (외래 키)")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Comment("상품 ID (외래 키)")
    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Comment("주문 수량")
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Comment("총 주문 금액")
    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    public static OrderDetail createOrderDetail(Order order, Long productId, int quantity, BigDecimal totalPrice) {
        return OrderDetail.builder()
                .order(order)
                .productId(productId)
                .quantity(quantity)
                .totalPrice(totalPrice)
                .build();
    }

}
