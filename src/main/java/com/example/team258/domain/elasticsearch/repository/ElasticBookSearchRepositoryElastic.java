package com.example.team258.domain.elasticsearch.repository;

import com.example.team258.domain.elasticsearch.entity.ElasticsearchBook;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ElasticBookSearchRepositoryElastic extends ElasticsearchRepository<ElasticsearchBook, Long>, ElasticCustomBookSearchRepository {
    List<ElasticsearchBook> findByBookNameContains(String keyword);
}

