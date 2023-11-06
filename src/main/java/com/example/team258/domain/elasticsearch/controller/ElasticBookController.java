package com.example.team258.domain.elasticsearch.controller;

import com.example.team258.domain.elasticsearch.application.ElasticBookResponse;
import com.example.team258.domain.elasticsearch.service.ElasticBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RequestMapping
@RestController
public class ElasticBookController {

    private final ElasticBookService elasticBookService;

    //@PostMapping("/users")
    //public ResponseEntity<Void> save(@RequestBody UserRequest userRequest) {
    //    UserRequestDto userRequestDto = new UserRequestDto(
    //        userRequest.getName(),
    //        userRequest.getDescription()
    //    );
    //    Long id = userService.save(userRequestDto);
    //    URI uri = URI.create(String.valueOf(id));
    //    return ResponseEntity.created(uri)
    //        .build();
    //}

    @GetMapping("search/el1")
    public ResponseEntity<List<ElasticBookResponse>> search(@RequestParam("book_name") String book_name, Pageable pageable) {
        List<ElasticBookResponse> elasticBookResponses = elasticBookService.searchByBookName(book_name, pageable)
                .stream()
                .map(ElasticBookResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(elasticBookResponses);
    }
}