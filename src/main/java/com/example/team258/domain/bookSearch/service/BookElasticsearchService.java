//package com.example.team258.domain.bookSearch.service;
//
//import com.example.team258.common.entity.Book;
//import com.example.team258.domain.bookSearch.repository.BookElasticsearchCustomRepository;
//import com.example.team258.domain.bookSearch.repository.BookElasticsearchRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class BookElasticsearchService {
//
//    private final BookElasticsearchRepository<Book, Long> bookElasticsearchRepository;
//    private final BookElasticsearchCustomRepository bookElasticsearchCustomRepository;
//
//    @Autowired
//    public BookElasticsearchService(BookElasticsearchRepository<Book, Long> bookElasticsearchRepository,
//                                    BookElasticsearchCustomRepository bookElasticsearchCustomRepository) {
//        this.bookElasticsearchRepository = bookElasticsearchRepository;
//        this.bookElasticsearchCustomRepository = bookElasticsearchCustomRepository;
//    }
//
//    public Page<Book> searchBooksByKeyword(String keyword, Pageable pageable) {
//        return bookElasticsearchRepository.searchBooksByKeyword(keyword, pageable);
//    }
//
//    public List<Book> searchBooksByAuthor(String author) {
//        return bookElasticsearchCustomRepository.searchBooksByAuthor(author);
//    }
//}
