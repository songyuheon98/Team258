//package com.example.team258.domain.bookSearch.repository;
//
//import com.example.team258.common.entity.Book;
//import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
//
//import java.util.List;
//
//public interface BookSearchRepository extends ElasticsearchRepository<Book, Long>, CustomBookSearchRepository {
//
//    List<Book> findByBasicBookInfo_NameContains(String name);
//}