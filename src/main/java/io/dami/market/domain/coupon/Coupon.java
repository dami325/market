package io.dami.market.domain.coupon;

import io.dami.market.domain.Auditor;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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
    @Column(name = "total_quantity")
    private int totalQuantity;

    @Comment("쿠폰 발급 수량")
    @Column(name = "issued_quantity")
    private int issuedQuantity;

    @Comment("쿠폰 만료일")
    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Builder.Default
    @OneToMany(mappedBy = "coupon", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<IssuedCoupon> issuedCoupons = new HashSet<>();

    public void issuedCoupon(Long userId) {
        checkExpiry();
        if (userId == null) {
            throw new IllegalArgumentException("유효하지 않은 사용자.");
        }
        if (this.issuedQuantity == this.totalQuantity) {
            throw new IllegalArgumentException("발급 수량 소진");
        }
        if (this.issuedCoupons.stream().anyMatch(issuedCoupon -> issuedCoupon.getUserId().equals(userId))) {
            throw new IllegalArgumentException("같은 쿠폰은 하나만 발급 가능합니다.");
        }
        this.issuedQuantity++;

        IssuedCoupon issuedCoupon = createIssuedCoupon(this, userId);
        this.issuedCoupons.add(issuedCoupon);
    }

    public void checkExpiry() {
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(this.endDate)) {
            throw new IllegalArgumentException("만료된 쿠폰입니다.");
        }
    }

    private IssuedCoupon createIssuedCoupon(Coupon coupon, Long userId) {
        return IssuedCoupon.builder()
                .coupon(coupon)
                .userId(userId)
                .issuedAt(LocalDateTime.now())
                .build();
    }

}
