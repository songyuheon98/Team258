package com.example.team258.domain.bookSearch.repository;

import com.example.team258.common.entity.Book;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface BookElasticsearchRepository extends ElasticsearchRepository<Book, Long> {
    // Elasticsearch와 관련된 추가적인 메서드가 필요하다면 여기에 추가하세요.
}