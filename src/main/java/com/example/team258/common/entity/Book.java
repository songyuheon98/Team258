package com.example.team258.common.entity;

import com.example.team258.domain.donation.entity.BookApplyDonation;
import com.example.team258.domain.donation.entity.BookDonationEvent;
import com.example.team258.domain.user.entity.BookRent;
import com.example.team258.domain.user.entity.BookReservation;
import com.example.team258.domain.admin.dto.AdminBooksRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;

@Entity(name = "book")
@Document(indexName = "books") // Elasticsearch 의 인덱스와 매핑하는 어노테이션
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Book {
//    @Version //낙관적락 적용
//    private Long version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long bookId;

    @Column(name = "book_name", nullable = false)
    private String bookName;

    @Column(name = "book_author", nullable = false)
    private String bookAuthor;

    @Column(name = "book_publish", nullable = false)
    private String bookPublish;

    @Column(name = "book_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private BookStatusEnum bookStatus;


    /**
     * 도서가 삭제 되었을때 나눔 신청도 삭제
     * casecade를 사용해서 도서 삭제시 나눔 신청도 삭제
     */
    @OneToOne(fetch = FetchType.LAZY)
//    @BatchSize(size = 100)
    @JoinColumn(name = "apply_id")
    private BookApplyDonation bookApplyDonation;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rent_id")
    private BookRent bookRent;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    private List<BookReservation> bookReservations;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_category_id")
    private BookCategory bookCategory;

    /**
     * 양방향
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "book_donation_event_id")
    private BookDonationEvent bookDonationEvent;

    public void changeStatus(BookStatusEnum bookStatus) {
        this.bookStatus = bookStatus;
    }

    public Book(AdminBooksRequestDto requestDto, BookCategory bookCategory) {
        this.bookName = requestDto.getBookName();
        this.bookAuthor = requestDto.getBookAuthor();
        this.bookPublish = requestDto.getBookPublish();
        this.bookCategory = bookCategory;
        this.bookStatus = BookStatusEnum.POSSIBLE; // 기본값은 대여 가능 상태로 설정
    }

    public void update(AdminBooksRequestDto requestDto, BookCategory bookCategory) {
        this.bookName = requestDto.getBookName();
        this.bookAuthor = requestDto.getBookAuthor();
        this.bookPublish = requestDto.getBookPublish();
        this.bookCategory = bookCategory;
        this.bookStatus = requestDto.getBookStatus();
    }

    public void addBookApplyDonation(BookApplyDonation bookApplyDonation){
        this.bookApplyDonation = bookApplyDonation;
    }

    public void removeBookApplyDonation(){
        this.bookApplyDonation = null;
    }

    public void addBookRent(BookRent bookRent){
        this.bookRent = bookRent;
    }

    public void addBookReservation(BookReservation bookReservation) {
        this.bookReservations.add(bookReservation);
    }

    public void updateBookCategory(BookCategory bookCategory) {
        this.bookCategory = bookCategory;
    }

    public void deleteRental() {
        this.bookRent = null;
    }

    public void addBookDonationEvent(BookDonationEvent bookDonationEvent) {
        this.bookDonationEvent = bookDonationEvent;
    }
    public void removeBookDonationEvent() {
        this.bookDonationEvent = null;
    }
}
