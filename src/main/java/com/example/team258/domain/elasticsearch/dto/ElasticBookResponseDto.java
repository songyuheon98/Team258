package com.example.team258.domain.elasticsearch.dto;

import com.example.team258.domain.elasticsearch.application.ElasticBookResponse;
import com.example.team258.domain.elasticsearch.entity.ElasticsearchBook;

public class ElasticBookResponseDto {

    private Long id;
    private String bookName;
    private String bookAuthor;

    private ElasticBookResponseDto() {
    }

    public ElasticBookResponseDto(Long id, String bookName, String bookAuthor) {
        this.id = id;
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
    }

    public static ElasticBookResponseDto from(ElasticsearchBook elasticsearchBook) {
        return new ElasticBookResponseDto(elasticsearchBook.getId(),elasticsearchBook.getBookName(),elasticsearchBook.getBookAuthor());
    }

    public Long getId() {
        return id;
    }

    public String getBookName() {
        return bookName;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }
}
