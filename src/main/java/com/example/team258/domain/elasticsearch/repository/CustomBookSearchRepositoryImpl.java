package com.example.team258.domain.elasticsearch.repository;

import com.example.team258.domain.elasticsearch.entity.ElasticsearchBook;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CustomBookSearchRepositoryImpl implements CustomBookSearchRepository {

    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    public List<ElasticsearchBook> findByBookNameContains(String bookName, Pageable pageable){
        Criteria criteria = Criteria.where("bookName").contains(bookName);
        Query query = new CriteriaQuery(criteria).setPageable(pageable);
        SearchHits<ElasticsearchBook> search = elasticsearchOperations.search(query, ElasticsearchBook.class);
        return search.stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }
}
