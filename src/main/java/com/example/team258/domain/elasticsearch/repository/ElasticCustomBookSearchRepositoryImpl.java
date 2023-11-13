package com.example.team258.domain.elasticsearch.repository;

import com.example.team258.domain.elasticsearch.entity.ElasticsearchBook;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ElasticCustomBookSearchRepositoryImpl implements ElasticCustomBookSearchRepository {

    private final ElasticsearchOperations elasticsearchOperations;

    //@Override
    //public List<ElasticsearchBook> findByBookNameContains(String keyword, Pageable pageable){ // 안녕 하세요  안녕%20하세요
    //    Criteria criteria = Criteria.where("bookName").contains(keyword);
    //    Query query = new CriteriaQuery(criteria).setPageable(pageable);
    //    SearchHits<ElasticsearchBook> searchHits = elasticsearchOperations.search(query, ElasticsearchBook.class);
    //    return searchHits.stream()
    //            .map(SearchHit::getContent)
    //            .collect(Collectors.toList());
    //}

    // 기본형
    //@Override
    //public List<ElasticsearchBook> findByBookNameContains(String keyword, Pageable pageable) {
    //    Query query = new NativeSearchQueryBuilder()
    //            .withQuery(QueryBuilders.matchPhraseQuery("bookName", keyword))
    //            .withPageable(pageable)
    //            .build();
    //
    //    SearchHits<ElasticsearchBook> searchHits = elasticsearchOperations.search(query, ElasticsearchBook.class);
    //
    //    return searchHits.stream()
    //            .map(SearchHit::getContent)
    //            .collect(Collectors.toList());
    //}

    // QueryStringQuery
    @Override
    public List<ElasticsearchBook> findByBookNameContains(String keyword, Pageable pageable) {
        log.info("Search keyword: {}", keyword);

        Query query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.queryStringQuery(keyword).field("bookName"))
                .withPageable(pageable)
                .build();

        SearchHits<ElasticsearchBook> searchHits = elasticsearchOperations.search(query, ElasticsearchBook.class);

        return searchHits.stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    //@Override
    //public List<ElasticsearchBook> findByBookNameContains(String keyword, Pageable pageable){
    //    String[] keywords = keyword.split("\\s+"); //%20  [0] , [1]
    //
    //    List<Criteria> criteriaList = Arrays.stream(keywords)
    //            .map(word -> Criteria.where("bookName").contains(word))
    //            .collect(Collectors.toList());
    //
    //    Criteria criteria = new Criteria();
    //    if (!criteriaList.isEmpty()) {
    //        criteria = criteriaList.get(0);
    //        for (int i = 1; i < criteriaList.size(); i++) {
    //            criteria.or(criteriaList.get(i));
    //        }
    //    }
    //
    //    Query query = new CriteriaQuery(criteria).setPageable(pageable);
    //    SearchHits<ElasticsearchBook> search = elasticsearchOperations.search(query, ElasticsearchBook.class);
    //
    //    return search.stream()
    //            .map(SearchHit::getContent)
    //            .collect(Collectors.toList());
    //}
}
