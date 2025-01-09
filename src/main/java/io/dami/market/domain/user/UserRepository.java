package io.dami.market.domain.user;

public interface UserRepository {
    User getUser(Long userId);
}
