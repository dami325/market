package io.dami.market.infra.coupon;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CouponQuerydslRepository {

    private final JPAQueryFactory queryFactory;
}
