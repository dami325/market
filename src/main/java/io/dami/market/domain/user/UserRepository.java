package io.dami.market.domain.user;

public interface UserRepository {
    User getUser(Long userId);

    User save(User 박주닮);
}
