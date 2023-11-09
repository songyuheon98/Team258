package com.example.team258;

import com.example.team258.domain.elasticsearch.config.ElasticSearchConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchClientAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(exclude = ElasticsearchClientAutoConfiguration.class)
@EnableJpaAuditing
public class Team258Application {

	public static void main(String[] args) {
		SpringApplication.run(Team258Application.class, args);
	}
}
