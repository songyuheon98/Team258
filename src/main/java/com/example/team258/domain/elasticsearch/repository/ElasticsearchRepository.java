//package com.example.team258.domain.elasticsearch.repository;
//
//import org.springframework.data.repository.NoRepositoryBean;
//import org.springframework.data.repository.PagingAndSortingRepository;
//
//@NoRepositoryBean
//public interface ElasticsearchRepository<T, ID> extends PagingAndSortingRepository<T, ID> {
//
//	Page<T> searchSimilar(T entity, @Nullable String[] fields, Pageable pageable);
//}