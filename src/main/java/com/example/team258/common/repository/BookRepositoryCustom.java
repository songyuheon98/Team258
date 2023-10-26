package com.example.team258.common.repository;

import com.example.team258.common.entity.BookCategory;
import com.example.team258.common.entity.Book;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface BookRepositoryCustom {
    Page<Book> findAllByCategoriesAndBookNameContaining2(@Param("bookCategories") List<BookCategory> bookCategories, @Param("keyword")String keyword, Pageable pageable);
}

