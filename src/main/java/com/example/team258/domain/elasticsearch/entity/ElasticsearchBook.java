package com.example.team258.domain.elasticsearch.entity;

import lombok.Getter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

@Document(indexName = "book")
@Getter
public class ElasticsearchBook {

    private Long id;

    @Field(name = "book_author")
    private String bookAuthor;

    @Field(name = "book_status")
    private String bookStatus;

    @Field(name = "book_publish")
    private String bookPublish;

    @Field(name = "book_name")
    private String bookName;

    // 나머지 필드들에 대한 매핑 추가
}

