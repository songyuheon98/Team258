package com.example.team258.domain.elasticsearch.dto;

//import com.example.team258.domain.elasticsearch.application.ElasticBookRequest;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ElasticBookRequestDto {

    private String bookName;
    private String bookAuthor;

    public ElasticBookRequestDto(String bookName, String bookAuthor) {
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
    }

    //public static ElasticBookRequestDto from(ElasticBookRequest elasticBookRequest) {
    //    return new ElasticBookRequestDto(
    //            elasticBookRequest.getBookName(),
    //            elasticBookRequest.getBookAuthor()
    //    );
    //}
}
