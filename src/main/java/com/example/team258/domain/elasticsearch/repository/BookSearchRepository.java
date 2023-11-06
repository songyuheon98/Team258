package com.example.team258.domain.elasticsearch.repository;

import com.example.team258.domain.elasticsearch.entity.ElasticsearchBook;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface BookSearchRepository extends ElasticsearchRepository<ElasticsearchBook, Long>, CustomBookSearchRepository {
    List<ElasticsearchBook> findByBookNameContains(String book_name);
}

