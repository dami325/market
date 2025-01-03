package io.dami.market.domain.point;

import io.dami.market.domain.Auditor;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;

@Entity
@Getter
@Builder
@AllArgsConstructor
@Table(name = "point_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointHistory extends Auditor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("지갑 이력 고유 ID")
    @Column(name = "id")
    private Long id;

    @Comment("지갑 ID (외래 키)")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id", nullable = false)
    private Point point;

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
