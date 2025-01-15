package io.dami.market.domain.user;

import io.dami.market.domain.Auditor;
import io.dami.market.domain.payment.PointNotEnoughException;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;

@Entity
@Getter
@Table(name = "user_point")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserPoint extends Auditor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Comment("잔액")
    @Column(name = "balance", nullable = false, precision = 10, scale = 2)
    private BigDecimal balance;

    public UserPoint(BigDecimal balance) {
        this.balance = balance;
    }

    public void subtract(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }
        if (this.balance.compareTo(amount) < 0) {
            throw new PointNotEnoughException("포인트 부족 사용 실패");
        }
        this.balance = this.balance.subtract(amount);
    }

    public void charge(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("0 이하는 충전불가");
        }
        this.balance = this.balance.add(amount);
    }
}
