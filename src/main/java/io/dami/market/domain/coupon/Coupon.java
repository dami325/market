package io.dami.market.domain.coupon;

import io.dami.market.domain.Auditor;
import io.dami.market.domain.user.User;
import io.dami.market.domain.user.UserCoupon;
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
    private Set<UserCoupon> userCoupons = new HashSet<>();

    public void issue(User user) {
        checkDate();
        if (user == null) {
            throw new IllegalArgumentException("유효하지 않은 사용자.");
        }
        if (this.issuedQuantity == this.totalQuantity) {
            throw new IllegalArgumentException("발급 수량 소진");
        }

        this.issuedQuantity++;

        user.addCoupon(this);
    }

    public void checkDate(){
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(endDate)) {
            throw new IllegalArgumentException("만료된 쿠폰입니다.");
        }
    }

}
