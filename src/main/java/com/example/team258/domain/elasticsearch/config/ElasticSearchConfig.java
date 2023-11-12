package com.example.team258.domain.elasticsearch.config;

import com.example.team258.domain.elasticsearch.repository.ElasticBookSearchRepository;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;


@EnableElasticsearchRepositories(basePackageClasses = {ElasticBookSearchRepository.class})
@Configuration
public class ElasticSearchConfig extends AbstractElasticsearchConfiguration {
    //@Value("${elasticsearch.hosts}")
    //private String elasticsearchHosts;

    @Bean
    public ElasticsearchOperations customElasticsearchTemplate(@Qualifier("elasticsearchClient") RestHighLevelClient client) {
        return new ElasticsearchRestTemplate(client);
    }
    @Override
    public RestHighLevelClient elasticsearchClient() {
        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                //.connectedTo("localhost:9200")
                .connectedTo("43.200.91.27:9200") // EC2 인스턴스의 퍼블릭 IPv4 주소로 변경
                .build();
        return RestClients.create(clientConfiguration).rest();
        // org.springframework.data.elasticsearch.client
        //    id 'org.springframework.boot' version '2.5.5'
        //    implementation 'org.springframework.data:spring-data-elasticsearch:4.2.2'
    }

}