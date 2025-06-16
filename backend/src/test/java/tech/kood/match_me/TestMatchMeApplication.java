package tech.kood.match_me;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

import tech.kood.match_me.feature.order.ProfileService;

@SpringBootTest
public class TestMatchMeApplication {

    public static void main(String[] args) {
        SpringApplication.from(MatchMeApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

    @Autowired
    ProfileService profileService;

    @Test
    void testOrderEvent() {
        profileService.createProfile("123-ABC", "Mimi");
    }

}
