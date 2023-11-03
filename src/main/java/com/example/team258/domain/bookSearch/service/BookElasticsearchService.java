package com.example.team258.domain.bookSearch.service;

import com.example.team258.common.dto.BookResponseDto;
import com.example.team258.common.entity.Book;
import com.example.team258.common.entity.BookCategory;
import com.example.team258.common.entity.QBook;
import com.example.team258.domain.bookSearch.repository.BookElasticsearchRepository;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookElasticsearchService {

    private BookElasticsearchRepository bookElasticsearchRepository;


    //더보기용 서비스
    public Slice<BookResponseDto> getMoreBooksByCategoryOrKeyword1(String bookCategoryName, String keyword, int page) {
        QBook qBook = QBook.book;
        BooleanBuilder builder = new BooleanBuilder();
        List<BookCategory> bookCategories = new ArrayList<>(); // 초기화

        if (bookCategoryName != null) {
            BookCategory bookCategory = bookCategoryRepository.findByBookCategoryName(bookCategoryName);
            if (bookCategory != null) { // null 체크 추가
                bookCategories = saveAllCategories(bookCategory);
            }
        }

        if (keyword != null)
            builder.and(qBook.bookName.contains(keyword));
        if (!bookCategories.isEmpty()) // 리스트가 비어있는지 확인
            builder.and(qBook.bookCategory.in(bookCategories));

        Sort sort = Sort.by(Sort.Direction.ASC, "bookId");
        Pageable pageable = PageRequest.of(page, 20, sort);

        // Slice로 추가 로드 데이터 가져오기
        Slice<BookResponseDto> bookList = bookElasticsearchRepository.findAllSliceBooks(builder, pageable).map(BookResponseDto::new);
        System.out.println(bookList.hasNext());

        return bookList;
    }

    // 추가적인 Elasticsearch 검색 로직이나 서비스 메서드들을 정의할 수 있습니다.
}

