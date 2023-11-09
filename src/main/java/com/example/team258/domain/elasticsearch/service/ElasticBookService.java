package com.example.team258.domain.elasticsearch.service;

import com.example.team258.common.repository.BookRepository;
import com.example.team258.domain.elasticsearch.dto.ElasticBookResponseDto;
import com.example.team258.domain.elasticsearch.repository.ElasticBookSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ElasticBookService {

    private final BookRepository bookRepository;
    private final ElasticBookSearchRepository elasticBookSearchRepository;

    public List<ElasticBookResponseDto> searchByBookName(String keyword, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.ASC, "book_id");
        //Sort sort = Sort.by(Sort.Direction.ASC, "book_name");
        Pageable pageable = PageRequest.of(page, size, sort);

        if (keyword == null) {
            // 검색 키워드가 null인 경우에는 전체 도서를 가져옴
            return elasticBookSearchRepository.findAll(pageable)
                    .stream()
                    .map(ElasticBookResponseDto::from)
                    .collect(Collectors.toList());
        }

        return elasticBookSearchRepository.findByBookNameContains(keyword, pageable)
            .stream()
            .map(ElasticBookResponseDto::from)
            .collect(Collectors.toList());
    }
}
