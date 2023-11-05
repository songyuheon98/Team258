//package com.example.team258.domain.bookSearch.controller;
//
//import com.example.team258.common.entity.Book;
//import com.example.team258.domain.bookSearch.service.BookElasticsearchService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//public class BookElasticsearchController {
//
//    private final BookElasticsearchService bookElasticsearchService;
//
//    @Autowired
//    public BookElasticsearchController(BookElasticsearchService bookElasticsearchService) {
//        this.bookElasticsearchService = bookElasticsearchService;
//    }
//
//    @GetMapping("/api/books/search")
//    public ResponseEntity<Page<Book>> searchBooksByKeyword(@RequestParam String keyword, Pageable pageable) {
//        Page<Book> result = bookElasticsearchService.searchBooksByKeyword(keyword, pageable);
//        return ResponseEntity.ok(result);
//    }
//
//    @GetMapping("/api/books/searchByAuthor")
//    public ResponseEntity<List<Book>> searchBooksByAuthor(@RequestParam String author) {
//        List<Book> result = bookElasticsearchService.searchBooksByAuthor(author);
//        return ResponseEntity.ok(result);
//    }
//}
