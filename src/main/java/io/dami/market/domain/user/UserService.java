package io.dami.market.domain.user;

import io.dami.market.domain.coupon.Coupon;
import io.dami.market.domain.coupon.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final CouponRepository couponRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<Coupon> getUserCoupons(Long userId) {
        return couponRepository.getCouponsByUserId(userId);
    }

    @Transactional(readOnly = true)
    public User getUser(Long userId) {
        return userRepository.getUser(userId);
    }
}
