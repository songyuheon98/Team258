package com.example.team258.domain.bookSearch.repository;

import com.example.team258.common.entity.Book;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface BookElasticsearchCustomRepository {

    List<Book> searchBooksByAuthor(String author);
}