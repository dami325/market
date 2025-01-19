package io.dami.market.domain.user;

import io.dami.market.domain.coupon.Coupon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<Coupon> getUserCoupons(Long userId) {
        User user = userRepository.getUser(userId);
        return user.getUserCoupons()
                .stream()
                .map(UserCoupon::getCoupon)
                .toList();
    }

    @Transactional(readOnly = true)
    public User getUser(Long userId) {
        return userRepository.getUser(userId);
    }
}
