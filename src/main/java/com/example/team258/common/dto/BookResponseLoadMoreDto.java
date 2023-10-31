package com.example.team258.common.dto;

import lombok.Data;

import java.util.List;
@Data
public class BookResponseLoadMoreDto {

    private List<BookResponseDto> bookResponseDtos;
    public BookResponseLoadMoreDto(List<BookResponseDto> bookResponseDtos){
        this.bookResponseDtos = bookResponseDtos;
    }
}
