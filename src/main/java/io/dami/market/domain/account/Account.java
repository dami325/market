package io.dami.market.domain.account;

import io.dami.market.domain.Auditor;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;

@Entity
@Getter
@Builder
@AllArgsConstructor
@Table(name = "tb_account")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends Auditor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment(value = "고유 아이디")
    @Column(name = "id")
    private Long id;

    @Comment("잔액")
    @Builder.Default
    @Column(name = "balance", nullable = false, precision = 19, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;
}
