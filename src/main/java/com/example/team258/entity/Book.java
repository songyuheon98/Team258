package com.example.team258.entity;

import com.example.team258.dto.AdminBooksRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "book")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long bookId;

    @Column(name = "book_name", nullable = false)
    private String bookName;

    @Column(name = "book_info", nullable = false)
    private String bookInfo;

    @Column(name = "book_author", nullable = false)
    private String bookAuthor;

    @Column(name = "book_publish", nullable = false)
    private LocalDateTime bookPublish;

    @Column(name = "book_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private BookStatusEnum bookStatus;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apply_id")
    private BookApplyDonation bookApplyDonation;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rent_id")
    private BookRent bookRent;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_reservation_id")
    private List<BookReservation> bookReservations;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_category_id")
    private BookCategory bookCategory;

    public void changeStatus(BookStatusEnum bookStatus) {
        this.bookStatus = bookStatus;
    }

    public Book(AdminBooksRequestDto requestDto, BookCategory bookCategory) {
        this.bookName = requestDto.getBookName();
        this.bookInfo = requestDto.getBookInfo();
        this.bookAuthor = requestDto.getBookAuthor();
        this.bookPublish = requestDto.getBookPublish();
        this.bookCategory = bookCategory;
        this.bookStatus = BookStatusEnum.POSSIBLE; // 기본값은 대여 가능 상태로 설정
    }

    public void update(AdminBooksRequestDto requestDto, BookCategory bookCategory) {
        this.bookName = requestDto.getBookName();
        this.bookInfo = requestDto.getBookInfo();
        this.bookAuthor = requestDto.getBookAuthor();
        this.bookPublish = requestDto.getBookPublish();
        this.bookCategory = bookCategory;
        this.bookStatus = requestDto.getBookStatus(); // 기본값은 대여 가능 상태로 설정
    }
    //public void addBookRent(BookRent bookRent){
    //    this.bookRent = bookRent;
    //    bookRent.addBook(this);
    //}

    public void addBookRent(BookRent bookRent){
        this.bookRent = bookRent;
    }

    public void addBookReservation(BookReservation bookReservation) {
        this.bookReservations.add(bookReservation);
    }
}
