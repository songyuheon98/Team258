package com.example.team258;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.team258.common.repository")
@EnableElasticsearchRepositories(basePackages = "com.example.team258.domain.bookSearch.repository")

public class Team258Application {

	public static void main(String[] args) {
		SpringApplication.run(Team258Application.class, args);
	}
}
