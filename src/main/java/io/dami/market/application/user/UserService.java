package io.dami.market.application.user;

import io.dami.market.domain.coupon.Coupon;
import io.dami.market.domain.user.User;
import io.dami.market.domain.user.UserCoupon;
import io.dami.market.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
