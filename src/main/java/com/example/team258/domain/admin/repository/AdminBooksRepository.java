package com.example.team258.domain.admin.repository;

import com.example.team258.common.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminBooksRepository extends JpaRepository<Book, Long>, QuerydslPredicateExecutor<Book> {

    Optional<Book> findByBookName(String bookName);

    // 초기 JPA 단계에서의 검색페이징 조회 기능 코드
    //Page<Book> findByBookNameContainingIgnoreCase(String keyword, Pageable pageable);

    // JPA 단계 + JPQL 어노테이션 방식의 검색페이징 조회 기능 코드
    //@Query("SELECT b FROM book b WHERE LOWER(b.bookName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    //Page<Book> findByBookNameContainingIgnoreCase(@Param("keyword") String keyword, Pageable pageable);

    // QueryDSL 단계에서는 QuerydslPredicateExecutor을 상속 받게 되는데
    // 내부적으로 Predicate라는 검색 조건을 나타내는 인터페이스가 있음
    // Predicate는 일종의 필터 역할을 하며, 쿼리에 적용할 다양한 조건을 표현해줌
    // 여기에 JPA 심플메소드만으로도 Predicate가 적용된 튜닝 쿼리가 생성되어 Repository를 코드를 간략하게 만들어줌
}
