package com.example.team258.common.repository;

import com.example.team258.common.entity.Book;
import com.example.team258.common.entity.BookCategory;
import com.example.team258.common.entity.QBook;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomBookRepositoryImpl implements CustomBookRepository{
    @Autowired
    private JPAQueryFactory queryFactory;

    @Override
    public Slice<Book> findAllSliceBooks(BooleanBuilder builder, Pageable pageable) {
        QBook book = QBook.book;
        List<Book> results = queryFactory.selectFrom(book)
                .where(builder)
                .limit(pageable.getPageSize()+1)
                .orderBy(book.bookId.asc())
                .offset(pageable.getOffset())
                .fetch();
        boolean hasNext = false;
        if(results.size() > pageable.getPageSize()){
            results.remove(results.size()-1);
            hasNext = true;
        }
        return new SliceImpl<>(results,pageable,hasNext);
    }
}
