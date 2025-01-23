package io.dami.market.infra.dummy;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("local")
@RequiredArgsConstructor
public class LocalDataInitializer {

    private final LocalDummyData localDummyData;

    @PostConstruct
    public void init() {
        localDummyData.init();
    }
}
