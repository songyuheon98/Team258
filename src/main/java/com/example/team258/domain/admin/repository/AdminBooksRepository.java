package com.example.team258.domain.admin.repository;

import com.example.team258.common.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminBooksRepository extends JpaRepository<Book, Long>, QuerydslPredicateExecutor<Book> {
    Page<Book> findAll(Specification<Book> spec, Pageable pageable);

    Optional<Book> findByBookName(String bookName);

    // 초기 JPA 단계에서의 검색페이징 조회 기능 코드
    Page<Book> findByBookNameContainingIgnoreCase(String keyword, Pageable pageable);
}
