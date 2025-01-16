package io.dami.market.infra.user;

import io.dami.market.domain.user.User;
import io.dami.market.domain.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public User getUser(Long userId) {
        return userJpaRepository.findByIdWithFetch(userId)
                .orElseThrow(() -> new EntityNotFoundException("유효하지 않은 사용자."));
    }

    @Override
    public User getUserWithLock(Long userId) {
        return userJpaRepository.findByIdWithLock(userId)
                .orElseThrow(() -> new EntityNotFoundException("유효하지 않은 사용자."));
    }

    @Override
    public User save(User user) {
        return userJpaRepository.save(user);
    }
}
