package com.example.team258.domain.elasticsearch.controller;

import com.example.team258.domain.elasticsearch.dto.ElasticBookResponseDto;
import com.example.team258.domain.elasticsearch.service.ElasticBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RequestMapping
@Controller
public class ElasticBookSearchController {

    private final ElasticBookService elasticBookService;

    // Elasticsearch + 무한스크롤 기능 구현 초기 페이지 진입
    //@GetMapping("/search/el1")
    @GetMapping("/")
    public String elasticSearchResults(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(value = "size", defaultValue = "20", required = false) Integer size,
            Model model) {

        long startTime = System.currentTimeMillis(); // 실행시간 측정

        // 페이지 요청 시, 미검색 기본 페이지를 가져오도록
        List<ElasticBookResponseDto> elasticBookResponseDtos = elasticBookService.searchByBookName(keyword, page, size);

        model.addAttribute("currentPage", page);
        model.addAttribute("books", elasticBookResponseDtos);
        model.addAttribute("hasNext", !elasticBookResponseDtos.isEmpty());

        long endTime = System.currentTimeMillis();
        long durationTimeSec = endTime - startTime;
        System.out.println(durationTimeSec + "m/s"); // 실행시간 측정

        return "users/searchEL1";
    }

    // Elasticsearch 무한 스크롤 데이터 요청 API
    @GetMapping("search/elastic")
    @ResponseBody
    public ResponseEntity<List<ElasticBookResponseDto>> search(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(value = "size", defaultValue = "20", required = false) Integer size) {
        long startTime = System.currentTimeMillis(); // 실행시간 측정

        List<ElasticBookResponseDto> elasticBookResponses = elasticBookService.searchByBookName(keyword, page, size)
                .stream()
                .map(ElasticBookResponseDto::from)
                .collect(Collectors.toList());
        long endTime = System.currentTimeMillis();
        long durationTimeSec = endTime - startTime;
        System.out.println(durationTimeSec + "m/s"); // 실행시간 측정
        return ResponseEntity.ok(elasticBookResponses);
    }
}