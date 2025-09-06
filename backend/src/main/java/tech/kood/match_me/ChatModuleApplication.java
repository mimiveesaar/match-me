package tech.kood.match_me;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "tech.kood.match_me.chatspace") // Only scan chat module
@EnableJpaRepositories(basePackages = "tech.kood.match_me.chatspace.repository") // Only scan chat repos
public class ChatModuleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatModuleApplication.class, args);
    }
}
