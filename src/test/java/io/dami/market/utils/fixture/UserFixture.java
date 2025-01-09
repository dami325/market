package io.dami.market.utils.fixture;

import io.dami.market.domain.user.User;
import io.dami.market.domain.user.UserPoint;

import java.math.BigDecimal;

public record UserFixture() {

    public static User user(String username){
        return User.builder()
                .username(username)
                .phoneNumber("010-1234-5678")
                .userPoint(new UserPoint(BigDecimal.valueOf(10000)))
                .email("wnekfa1004@naver.com")
                .build();
    }

    public static User user(String username,int point){
        return User.builder()
                .username(username)
                .phoneNumber("010-1234-5678")
                .userPoint(new UserPoint(BigDecimal.valueOf(point)))
                .email("wnekfa1004@naver.com")
                .build();
    }

}
