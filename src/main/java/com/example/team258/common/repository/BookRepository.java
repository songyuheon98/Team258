package com.example.team258.common.repository;

import com.example.team258.common.entity.Book;
import com.example.team258.domain.user.entity.BookRent;
import com.example.team258.common.entity.BookStatusEnum;
import com.example.team258.common.entity.BookCategory;

import jakarta.persistence.LockModeType;

import com.querydsl.core.BooleanBuilder;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
public interface BookRepository extends JpaRepository <Book,Long>, QuerydslPredicateExecutor<Book>, BookRepositoryCustom {

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("SELECT b from book b WHERE b.bookId = :bookId")
    Optional<Book> findByIdLock(@Param("bookId") Long bookId);

    Page<Book> findAllByBookNameContaining(String keyword, Pageable pageable);

    @Query("SELECT b FROM book b WHERE b.bookCategory IN :bookCategories")
    Page<Book> findAllByCategories(@Param("bookCategories") List<BookCategory> bookCategories, Pageable pageable);


    @Query("SELECT b FROM book b WHERE b.bookCategory IN :bookCategories AND b.bookName like %:keyword%")
    Page<Book> findAllByCategoriesAndBookNameContaining(@Param("bookCategories") List<BookCategory> bookCategories, @Param("keyword")String keyword, Pageable pageable);

    List<Book> findByBookStatus(BookStatusEnum bookStatus);

    Page<Book>findPageByBookStatus(BookStatusEnum bookStatusEnum,Pageable pageable);
    Optional<Book> findByBookRent(BookRent bookRent);

    @Query(value = "select b from book b" +
            " where b.bookDonationEvent.donationId = :donationId and b.bookStatus = :status")
    Page<Book> findBooksByDonationId(@Param("donationId") Long donationId, @Param("status") BookStatusEnum status, Pageable pageable);

    @Query(value = "select b from book b" +
            " where b.bookDonationEvent.donationId = :donationId")
    Page<Book> findBooksNoStatusByDonationId(@Param("donationId") Long donationId , Pageable pageable);

    @Query("select count(b) FROM book b")
    Long getMaxCount();


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select b from book b " +
            "left join fetch b.bookApplyDonation  " +
            "where b.bookId = :bookId")
    Optional<Book> findByIdFetch(@Param("bookId") Long bookId);

    @Query("select b from book b " +
            "left join fetch b.bookRent  " +
            "where b.bookId = :bookId")
    Optional<Book> findByIdFetchBookRent(@Param("bookId") Long bookId);

//
//        @Query(value = "SELECT b FROM book b ",
//                nativeQuery = false)
//        Slice<Book> findAllSliceBooks(BooleanBuilder builder,Pageable pageable);

    @Query(value = "SELECT * FROM book b WHERE MATCH(b.book_name) AGAINST(:tmp IN boolean mode) AND b.bookCategory IN :bookCategories", nativeQuery = true)
    Slice<Book> findAllByCategoriesAndBookNameContainingFTI(Pageable pageable, List<BookCategory> bookCategories, String tmp);

    @Query(value = "SELECT * FROM book b WHERE MATCH(b.book_name) AGAINST(:tmp IN boolean mode)", nativeQuery = true)
    Slice<Book> findAllByBookNameContainingFTI(Pageable pageable, String tmp);

}
