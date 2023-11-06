package com.example.team258.domain.elasticsearch.application;

public class ElasticBookRequest {

    private String book_name;
    private String bookAuthor;

    private ElasticBookRequest() {
    }

    public ElasticBookRequest(String book_name, String bookAuthor) {
        this.book_name = book_name;
        this.bookAuthor = bookAuthor;
    }

    public String getBookName() {
        return book_name;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }
}
