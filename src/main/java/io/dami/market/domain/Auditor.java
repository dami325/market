package io.dami.market.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@DynamicInsert
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class Auditor {

    //등록일
    @Column(name = "created_date", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdDate;

    //최종 수정일
    @Column(name = "modified_date")
    @LastModifiedDate
    private LocalDateTime modifiedDate;


}
