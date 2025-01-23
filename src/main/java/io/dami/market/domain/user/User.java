package io.dami.market.domain.user;

import io.dami.market.domain.Auditor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
  @Column(name = "email")
  private String email;

  @Comment("전화번호")
  @Column(name = "phone_number")
  private String phoneNumber;

}
