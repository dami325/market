package io.dami.market.domain.point;

import io.dami.market.domain.Auditor;
import io.dami.market.domain.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;

@Entity
@Getter
@Builder
@AllArgsConstructor
@Table(name = "point")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Point extends Auditor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("지갑 고유 ID")
    @Column(name = "id")
    private Long id;

    @Comment("사용자 ID (외래 키)")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Comment("잔액")
    @Column(name = "balance", nullable = false, precision = 10, scale = 2)
    private BigDecimal balance;

}
