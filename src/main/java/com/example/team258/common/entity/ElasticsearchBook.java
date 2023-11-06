package com.example.team258.common.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "book")
public class ElasticsearchBook {

    @Id
    @Field(type = FieldType.Long, name = "book_id")
    private Long bookId;

    @Field(type = FieldType.Text, name = "book_author")
    private String bookAuthor;

    @Field(type = FieldType.Keyword, name = "book_status")
    private String bookStatus;

    @Field(type = FieldType.Keyword, name = "book_publish")
    private String bookPublish;

    @Field(type = FieldType.Keyword, name = "book_name")
    private String bookName;

    // 나머지 필드들에 대한 매핑 추가
}

