package com.example.team258.domain.elasticsearch.repository;

import com.example.team258.domain.elasticsearch.entity.ElasticsearchBook;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomBookSearchRepository {
    List<ElasticsearchBook> findByBookNameContains(String book_name, Pageable pageable);

}
