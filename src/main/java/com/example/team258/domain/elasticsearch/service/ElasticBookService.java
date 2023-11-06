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

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ElasticBookService {

    private final BookRepository bookRepository;
    private final ElasticBookSearchRepositoryElastic elasticBookSearchRepository;

    public List<ElasticBookResponseDto> searchByBookName(String keyword, int page, int size) {
        if (keyword == null) {
            // 검색 키워드가 null인 경우에 대한 처리
            // 예를 들어, 빈 리스트를 반환하거나 다른 처리를 수행할 수 있습니다.
            return Collections.emptyList();
        }
        Sort sort = Sort.by(Sort.Direction.ASC, "_id");
        Pageable pageable = PageRequest.of(page, size, sort);

        return elasticBookSearchRepository.findByBookNameContains(keyword, pageable)
            .stream()
            .map(ElasticBookResponseDto::from)
            .collect(Collectors.toList());
    }
}
