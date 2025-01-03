package io.dami.market.domain.coupon;

import io.dami.market.domain.Auditor;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@Table(name = "coupon")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon extends Auditor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("쿠폰 고유 ID")
    @Column(name = "id")
    private Long id;

    @Comment("쿠폰 이름")
    @Column(name = "name", nullable = false)
    private String name;

    @Comment("할인 금액")
    @Column(name = "discount_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal discountAmount;

    @Comment("쿠폰 총 수량")
    @Column(name = "total_quantity", nullable = false)
    private Integer totalQuantity;

    @Comment("쿠폰 발급 수량")
    @Column(name = "issued_quantity", nullable = false)
    private Integer issuedQuantity;

    @Comment("쿠폰 사용 시작일")
    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Comment("쿠폰 사용 종료일")
    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

}
