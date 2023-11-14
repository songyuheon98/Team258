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

    //크리테리아는 복잡도가 높아서 사용하지 않는다.
    //@Override
    //public List<ElasticsearchBook> findByBookNameContains(String keyword, Pageable pageable){ // 안녕 하세요  안녕%20하세요
    //    Criteria criteria = Criteria.where("bookName").contains(keyword);
    //    Query query = new CriteriaQuery(criteria).setPageable(pageable);
    //    SearchHits<ElasticsearchBook> searchHits = elasticsearchOperations.search(query, ElasticsearchBook.class);
    //    return searchHits.stream()
    //            .map(SearchHit::getContent)
    //            .collect(Collectors.toList());
    //}

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




    // QueryDSL로 작성 할 경우 참고 자료
    // 의존성과 플러그인 등 세팅이 필요하다.

    /*
dependencies {
    implementation 'com.querydsl:querydsl-apt:4.x.x'
    implementation 'com.querydsl:querydsl-elasticsearch:4.x.x'
}

plugins {
    id "com.ewerk.gradle.plugins.querydsl" version "1.0.10"
}

querydsl {
    jpa = false
    querydslSourcesDir = 'src/main/generated'
}

sourceSets {
    main {
        java {
            srcDir 'src/main/generated'
        }
    }
}

configurations {
    querydsl.extendsFrom compileClasspath
}

compileQuerydsl {
    options.annotationProcessorPath = configurations.querydsl
}

*/

    // QueryDSL 구현체 모습은 다음과 같다.

    /*
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.elasticsearch.ElasticsearchQuery;
import com.querydsl.elasticsearch.ElasticsearchQueryFactory;

public class ElasticBookSearchRepositoryImpl {

    private final ElasticsearchQueryFactory queryFactory;

    public ElasticBookSearchRepositoryImpl(ElasticsearchQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public ElasticsearchQuery<ElasticsearchBook> findByQueryStringQuery(String queryString, Pageable pageable) {
        QElasticsearchBook elasticsearchBook = QElasticsearchBook.elasticsearchBook;
        StringPath bookName = elasticsearchBook.bookName;

        return queryFactory
                .selectFrom(elasticsearchBook)
                .where(bookName.contains(queryString))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());
    }
}
*/
}
