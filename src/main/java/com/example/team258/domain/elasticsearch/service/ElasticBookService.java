package com.example.team258.domain.elasticsearch.service;

import com.example.team258.common.repository.BookRepository;
import com.example.team258.domain.elasticsearch.dto.ElasticBookResponseDto;
import com.example.team258.domain.elasticsearch.repository.BookSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ElasticBookService {

    private final BookRepository bookRepository;
    private final BookSearchRepository bookSearchRepository;

    //@Transactional
    //public Long save(ElasticBookRequestDto elasticBookRequestDto) {
    //    ElasticsearchBook elasticsearchBook = new ElasticsearchBook(elasticBookRequestDto.getBookName(), elasticBookRequestDto.getAuthor());
    //    ElasticsearchBook savedBook = userRepository.save(elasticsearchBook);
    //    userSearchRepository.save(elasticsearchBook);
    //    return savedUser.getId();
    //}

    public List<ElasticBookResponseDto> searchByBookName(String bookName, Pageable pageable) {
        // userSearchRepository.findByBasicProfile_NameContains(name) 가능
        return bookSearchRepository.findByBookNameContains(bookName, pageable)
            .stream()
            .map(ElasticBookResponseDto::from)
            .collect(Collectors.toList());
    }
}
