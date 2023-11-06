package com.example.team258.domain.elasticsearch.config;

import com.example.team258.domain.elasticsearch.repository.ElasticBookSearchRepositoryElastic;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;


@EnableElasticsearchRepositories(basePackageClasses = {ElasticBookSearchRepositoryElastic.class})
@Configuration
public class ElasticSearchConfig extends AbstractElasticsearchConfiguration {
    @Bean
    public ElasticsearchOperations elasticsearchTemplate(@Qualifier("elasticsearchClient") RestHighLevelClient client) {
        return new ElasticsearchRestTemplate(client);
    }
    @Override
    public RestHighLevelClient elasticsearchClient() {
        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo("localhost:9200")
                .build();
        return RestClients.create(clientConfiguration).rest();
        // org.springframework.data.elasticsearch.client
        //    id 'org.springframework.boot' version '2.5.5'
        //    implementation 'org.springframework.data:spring-data-elasticsearch:4.2.2'
    }

}