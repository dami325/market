package io.dami.market.application.coupon;

import io.dami.market.domain.coupon.Coupon;
import io.dami.market.domain.coupon.CouponRepository;
import io.dami.market.domain.user.User;
import io.dami.market.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<Coupon> getFirstServedCoupons(Long userId) {
        userRepository.getUser(userId);
        return couponRepository.getFirstServedCoupons(userId);
    }

    @Transactional
    public void issueACoupon(Long couponId, Long userId) {
        Coupon coupon = couponRepository.getCouponWithLock(couponId);
        User user = userRepository.getUser(userId);
        coupon.issue(user);
    }
}
