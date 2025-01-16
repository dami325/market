package io.dami.market.domain.user;

public interface UserRepository {
    User getUser(Long userId);

    User getUserWithLock(Long userId);

    User save(User user);
}
