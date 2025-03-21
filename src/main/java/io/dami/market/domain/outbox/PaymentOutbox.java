package io.dami.market.domain.outbox;

import io.dami.market.domain.Auditor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(name = "payment_outbox")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentOutbox extends Auditor {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Comment(value = "고유 아이디")
  @Column(name = "id")
  private Long id;

  @Column(name = "payload")
  private String payload;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", columnDefinition = "varchar(255)")
  private PaymentOutboxStatus status;

  public static PaymentOutbox create(String payload) {
    return PaymentOutbox.builder()
        .payload(payload)
        .status(PaymentOutboxStatus.INIT)
        .build();
  }

  public void changeStatus(PaymentOutboxStatus paymentOutboxStatus) {
    this.status = paymentOutboxStatus;
  }

  public enum PaymentOutboxStatus {
    INIT, SUCCESS, FAIL
  }
}
