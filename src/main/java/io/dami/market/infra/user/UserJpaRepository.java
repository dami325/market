package io.dami.market.infra.user;

import io.dami.market.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, Long> {

    @Query("""
    select us
    from User us
    inner join fetch  us.userPoint
    left join fetch  us.userCoupons
    where us.id = :userId
    """)
    Optional<User> findByIdWithFetch(Long userId);
}
