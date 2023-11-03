package com.example.team258.domain.bookSearch.controller;

import com.example.team258.common.dto.BookResponseDto;
import com.example.team258.common.entity.Book;
import com.example.team258.domain.bookSearch.service.BookElasticsearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookElasticsearchController {

    @Autowired
    private BookElasticsearchService bookElasticsearchService;

    @PostMapping("/save-to-elasticsearch")
    public void saveBookToElasticsearch(@RequestBody Book book) {
        bookElasticsearchService.saveBookToElasticsearch(book);
    }

    @GetMapping("/search-in-elasticsearch")
    public ResponseEntity<Page<BookResponseDto>> searchBooksInElasticsearch(@RequestParam(value = "bookCategoryName", required = false) String bookCategoryName,
                                                                            @RequestParam(value = "keyword", required = false) String keyword,
                                                                            @RequestParam(value = "page") int page) {
        return bookElasticsearchService.getMoreBooksByCategoryOrKeyword1(bookCategoryName,keyword,page-1);
    }

    // 추가적인 컨트롤러 메서드들을 정의할 수 있습니다.
}
