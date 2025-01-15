package io.dami.market.domain.user;

import io.dami.market.domain.Auditor;
import io.dami.market.domain.coupon.Coupon;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Builder
@AllArgsConstructor
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends Auditor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("사용자 고유 ID")
    @Column(name = "id")
    private Long id;

    @Builder.Default
    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "point_id", nullable = false)
    private UserPoint userPoint = new UserPoint(BigDecimal.ZERO);

    @Comment("사용자 이름")
    @Column(name = "username")
    private String username;

    @Comment("이메일")
    @Column(name = "email", unique = true)
    private String email;

    @Comment("전화번호")
    @Column(name = "phone_number")
    private String phoneNumber;

    @Builder.Default
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<UserCoupon> userCoupons = new HashSet<>();

    public void usePoint(BigDecimal amount) {
        this.userPoint.subtract(amount);
    }

    public void chargePoint(BigDecimal amount) {
        this.userPoint.charge(amount);
    }

    public void addCoupon(Coupon coupon) {
        boolean isAlreadyIssued = this.userCoupons.stream().anyMatch(userCoupon -> userCoupon.getCoupon().equals(coupon));
        if (isAlreadyIssued) {
            throw new IllegalArgumentException("같은 쿠폰은 하나만 발급 가능합니다.");
        }
        this.userCoupons.add(UserCoupon.builder()
                .coupon(coupon)
                .user(this)
                .issuedAt(LocalDateTime.now())
                .build());
    }
}
