package com.example.team258.domain.bookSearch.controller;

import com.example.team258.common.dto.BookResponseDto;
import com.example.team258.domain.bookSearch.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;

    @GetMapping
    public ResponseEntity<Page<BookResponseDto>> getAllBooks(@RequestParam("page") int page) {
        return ResponseEntity.ok(searchService.getAllBooks(page - 1));
    }

    @GetMapping("/v2")
    public ResponseEntity<List<BookResponseDto>> getAllBooksSlice(@RequestParam("page") int page, @RequestParam("size") @DefaultValue("10") int size) {
        Slice<BookResponseDto> booksSlice = searchService.getAllBooksV2(page, size);
        return ResponseEntity.ok(booksSlice.getContent());
    }


    @GetMapping("/{bookId}")
    public ResponseEntity<BookResponseDto> getBookById(@PathVariable Long bookId) {
        return ResponseEntity.ok(searchService.getBookById(bookId));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<BookResponseDto>> getAllBooksByCategoryOrKeyword(@RequestParam(value = "bookCategoryName", required = false) String bookCategoryName,
                                                                                @RequestParam(value = "keyword", required = false) String keyword,
                                                                                @RequestParam(value = "page") int page) {
        if(bookCategoryName==null){
            return ResponseEntity.ok(searchService.getAllBooksByKeyword(keyword,page-1));
        } else if(keyword == null){
            return ResponseEntity.ok(searchService.getAllBooksByCategory(bookCategoryName,page-1));
        }
        return ResponseEntity.ok(searchService.getAllBooksByCategoryOrKeyword(bookCategoryName,keyword,page-1));
    }

    //@GetMapping("/search-lm1")
    //public ResponseEntity<Slice<BookResponseDto>> getMoreBooksByCategoryOrKeyword(@RequestParam(value = "bookCategoryName", required = false) String bookCategoryName,
    //                                                                             @RequestParam(value = "keyword", required = false) String keyword,
    //                                                                             @RequestParam(value = "page") int page) {
    //    if(bookCategoryName==null){
    //        return ResponseEntity.ok(searchService.getAllBooksByKeyword(keyword,page-1));
    //    } else if(keyword == null){
    //        return ResponseEntity.ok(searchService.getAllBooksByCategory(bookCategoryName,page-1));
    //    }
    //
    //    // 추가된 부분: 두 번째 조회에 대한 로그
    //    System.out.println("Controller - getMoreBooksByCategoryOrKeyword: bookCategoryName=" + bookCategoryName + ", keyword=" + keyword + ", page=" + (page - 1));
    //
    //    return ResponseEntity.ok(searchService.getMoreBooksByCategoryOrKeyword(bookCategoryName, keyword, page - 1));
    //}

}
