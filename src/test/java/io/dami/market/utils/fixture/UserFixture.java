package io.dami.market.utils.fixture;

import io.dami.market.domain.user.User;

public record UserFixture() {

    public static User user(String username) {
        return User.builder()
                .username(username)
                .phoneNumber("010-1234-5678")
                .email("wnekfa1004@naver.com")
                .build();
    }

    public static User user(Long id,String username) {
        return User.builder()
                .id(id)
                .username(username)
                .phoneNumber("010-1234-5678")
                .email("wnekfa1004@naver.com")
                .build();
    }

}
