package com.example.team258.common.repository;

import com.example.team258.common.entity.ElasticsearchBook;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;


/**
 * 엘라스틱 서치를 사용해서 도서 검색을 위한 기본 레포지토리입니다.
 * */

public interface ElasticBookRepository extends ElasticsearchRepository<ElasticsearchBook, Long> {
    List<ElasticsearchBook> findByBookNameContaining(String keyword);
}