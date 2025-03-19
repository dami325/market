package io.dami.market.domain.coupon;

import io.dami.market.domain.Auditor;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Builder
@AllArgsConstructor
@Table(name = "issued_coupon")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IssuedCoupon extends Auditor {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Comment("쿠폰 이력 고유 ID")
  @Column(name = "id")
  private Long id;

  @Comment("사용자 ID (외래 키)")
  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Comment("쿠폰 ID (외래 키)")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "coupon_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
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
