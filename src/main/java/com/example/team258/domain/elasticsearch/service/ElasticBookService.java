package com.example.team258.domain.elasticsearch.service;

import com.example.team258.common.repository.BookRepository;
import com.example.team258.domain.elasticsearch.dto.ElasticBookResponseDto;
import com.example.team258.domain.elasticsearch.repository.ElasticBookSearchRepositoryElastic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final ElasticBookSearchRepositoryElastic elasticBookSearchRepository;

    public List<ElasticBookResponseDto> searchByBookName(String keyword, int page) {

        Sort sort = Sort.by(Sort.Direction.ASC, "_id");
        Pageable pageable = PageRequest.of(page, 10, sort);

        return elasticBookSearchRepository.findByBookNameContains(keyword, pageable)
            .stream()
            .map(ElasticBookResponseDto::from)
            .collect(Collectors.toList());
    }
}
