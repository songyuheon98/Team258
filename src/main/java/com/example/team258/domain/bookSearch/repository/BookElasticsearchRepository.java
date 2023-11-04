package com.example.team258.domain.bookSearch.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

@NoRepositoryBean
public interface BookElasticsearchRepository<T, ID> extends PagingAndSortingRepository<T, ID> {

    Page<T> searchBooksByKeyword(String keyword, Pageable pageable);
}