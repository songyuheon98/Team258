package com.example.team258.common.repository;

import com.example.team258.common.entity.Book;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomBookRepository {
    Slice<Book> findAllSliceBooks(BooleanBuilder builder, Pageable pageable);
}
