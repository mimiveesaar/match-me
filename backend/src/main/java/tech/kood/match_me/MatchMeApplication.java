package tech.kood.match_me;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class MatchMeApplication {

	public static void main(String[] args) {
		SpringApplication.run(MatchMeApplication.class, args);
	}
}