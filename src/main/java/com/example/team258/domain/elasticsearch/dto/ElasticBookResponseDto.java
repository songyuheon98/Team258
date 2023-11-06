package com.example.team258.domain.elasticsearch.dto;

import com.example.team258.domain.elasticsearch.entity.ElasticsearchBook;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ElasticBookResponseDto {

    private Long bookId;
    private String bookName;
    private String bookAuthor;
    private String bookPublish;
    private String bookStatus;

    public ElasticBookResponseDto(Long id, String bookName, String bookAuthor, String bookPublish, String bookStatus) {
        this.bookId = id;
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
        this.bookPublish = bookPublish;
        this.bookStatus = bookStatus;
    }

    public static ElasticBookResponseDto from(ElasticsearchBook elasticsearchBook) {
        return new ElasticBookResponseDto(
                elasticsearchBook.getId(),
                elasticsearchBook.getBookName(),
                elasticsearchBook.getBookAuthor(),
                elasticsearchBook.getBookPublish(),
                elasticsearchBook.getBookStatus()
        );
    }

    public static ElasticBookResponseDto from(ElasticBookResponseDto elasticBookResponseDto) {
        return new ElasticBookResponseDto(
                elasticBookResponseDto.getBookId(),
                elasticBookResponseDto.getBookName(),
                elasticBookResponseDto.getBookAuthor(),
                elasticBookResponseDto.getBookPublish(),
                elasticBookResponseDto.getBookStatus()
        );
    }
}
