package io.dami.market.domain.user;

import io.dami.market.domain.Auditor;
import io.dami.market.domain.coupon.Coupon;
import io.dami.market.domain.coupon.IssuedCoupon;
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
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends Auditor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("사용자 고유 ID")
    @Column(name = "id")
    private Long id;

    @Comment("사용자 이름")
    @Column(name = "username")
    private String username;

    @Comment("이메일")
    @Column(name = "email", unique = true)
    private String email;

    @Comment("전화번호")
    @Column(name = "phone_number")
    private String phoneNumber;

}
