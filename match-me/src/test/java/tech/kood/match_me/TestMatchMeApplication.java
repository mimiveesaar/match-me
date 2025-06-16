package tech.kood.match_me;

import org.springframework.boot.SpringApplication;

public class TestMatchMeApplication {

	public static void main(String[] args) {
		SpringApplication.from(MatchMeApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
