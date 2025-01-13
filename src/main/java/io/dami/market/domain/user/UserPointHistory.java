package io.dami.market.domain.user;

import io.dami.market.domain.Auditor;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;

@Entity
@Getter
@Builder
@AllArgsConstructor
@Table(name = "user_point_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserPointHistory extends Auditor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_point_id", nullable = false)
    private UserPoint userPoint;

    @Comment("거래 금액")
    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Comment("거래 유형 (예: 충전, 차감)")
    @Column(name = "transaction_type", nullable = false)
    private String transactionType;

    @Comment("거래 설명")
    @Column(name = "description")
    private String description;

}
