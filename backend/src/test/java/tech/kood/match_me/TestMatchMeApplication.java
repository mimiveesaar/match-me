package tech.kood.match_me;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class TestMatchMeApplication {

    public static void main(String[] args) {
        SpringApplication.from(Application::main).with(TestcontainersConfiguration.class).run(args);
    }
}
