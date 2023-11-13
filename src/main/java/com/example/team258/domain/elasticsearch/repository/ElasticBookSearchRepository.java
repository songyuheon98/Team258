package com.example.team258.domain.elasticsearch.repository;

import com.example.team258.domain.elasticsearch.entity.ElasticsearchBook;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ElasticBookSearchRepository extends ElasticsearchRepository<ElasticsearchBook, Long>, ElasticCustomBookSearchRepository {
    // 이건 개발자가 직접 연산부를 작성하는 방식임
    // 제어권이 개발자에 있음
    //List<ElasticsearchBook> findByBookNameContains(String keyword);

    // Elasticsearch API의 쿼리문을 작성해서 변경해보는 방법
    // 이건 제어권이 Spring Data Elasticsearch 에 있음
    // 쿼리를 SPE가 분석함

    //QueryStringQuery - 엘라스틱서치 내장 기본 쿼리
    @Query("{\"query_string\": {\"query\": \"?0\"}}")
    List<ElasticsearchBook> findByQueryStringQuery(String queryString, Pageable pageable);

    //조건검색(bool)
    //@Query("{\"bool\": {\"must\": {\"query_string\": {\"query\": \"?0\"}}}}")
    //List<ElasticsearchBook> findByQueryStringQuery(String queryString, Pageable pageable);
        /*
        must: [필드]	AND [컬럼] = [조건]
        must_not: [필드]	AND [컬럼] != [조건]
        should: [필드]	OR [컬럼] = [조건]
        filter: [필드]	[컬럼] IN ( [조건] )
        */
}

