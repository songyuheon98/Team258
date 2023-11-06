package com.example.team258.domain.elasticsearch.dto;

public class ElasticBookRequestDto {

    private String bookName;
    private String bookAuthor;

    private ElasticBookRequestDto() {
    }

    public ElasticBookRequestDto(String bookName, String bookAuthor) {
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
    }

    public String getBookName() {
        return bookName;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }
}
