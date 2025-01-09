package io.dami.market.infra.user;

import io.dami.market.domain.user.User;
import io.dami.market.domain.user.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UserRepositoryImpl implements UserRepository {
    @Override
    public User getUser(Long userId) {
        return null;
    }
}
