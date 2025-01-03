package io.dami.market.domain.coupon;

import io.dami.market.domain.Auditor;
import io.dami.market.domain.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@Table(name = "coupon_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponHistory extends Auditor {

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
}
