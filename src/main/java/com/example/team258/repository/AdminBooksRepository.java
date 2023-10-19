package com.example.team258.repository;

import com.example.team258.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminBooksRepository extends JpaRepository<Book, Long> {
    Page<Book> findAll(Specification<Book> spec, Pageable pageable);
}
