package io.dami.market.domain.user;

import io.dami.market.domain.Auditor;
import io.dami.market.domain.coupon.Coupon;
import io.dami.market.domain.coupon.CouponAlreadyUsedException;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@Table(name = "user_coupon")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserCoupon extends Auditor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("쿠폰 이력 고유 ID")
    @Column(name = "id")
    private Long id;

    @Comment("사용자 ID (외래 키)")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Comment("쿠폰 ID (외래 키)")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;

    @Comment("쿠폰 발급 일시")
    @Column(name = "issued_at")
    private LocalDateTime issuedAt;

    @Comment("쿠폰 사용 일시")
    @Column(name = "used_at")
    private LocalDateTime usedAt;

    public BigDecimal useCoupon() {
        if (usedAt != null) {
            throw new CouponAlreadyUsedException("이미 사용한 쿠폰입니다.");
        }
        this.coupon.checkExpiry();
        this.usedAt = LocalDateTime.now();
        return this.coupon.getDiscountAmount();
    }
}
