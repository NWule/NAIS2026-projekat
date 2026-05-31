package NAIS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.config.EnableElasticsearchAuditing;

@SpringBootApplication
@EnableElasticsearchAuditing
public class PlayerInfoServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlayerInfoServiceApplication.class, args);
	}

}
