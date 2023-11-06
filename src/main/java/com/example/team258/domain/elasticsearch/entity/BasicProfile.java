package com.example.team258.domain.elasticsearch.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class BasicProfile {

    @Column(nullable = false, unique = true)
    private String bookName;

    private String bookAuthor;

    protected BasicProfile() {
    }

    public BasicProfile(String bookName, String bookAuthor) {
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
