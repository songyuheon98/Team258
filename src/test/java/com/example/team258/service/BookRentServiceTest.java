package com.example.team258.service;

import com.example.team258.domain.user.entity.BookRent;
import com.example.team258.domain.user.entity.BookReservation;
import com.example.team258.domain.user.service.BookRentService;
import com.example.team258.common.dto.MessageDto;
import com.example.team258.common.entity.Book;
import com.example.team258.common.entity.BookStatusEnum;
import com.example.team258.common.entity.User;
import com.example.team258.common.entity.UserRoleEnum;
import com.example.team258.domain.user.repository.BookRentRepository;
import com.example.team258.common.repository.BookRepository;
import com.example.team258.common.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc(addFilters = false)
class BookRentServiceTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRentService bookRentService;

    @MockBean
    private BookRentRepository bookRentRepository;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private UserRepository userRepository;

    User user1 = User.builder()
            .userId(1L)
            .username("유저1")
            .password("123")
            .role(UserRoleEnum.USER)
            .bookRents(new ArrayList<BookRent>())
            .bookReservations(new ArrayList<BookReservation>())
            .build();
    User user2 = User.builder()
            .userId(1L)
            .username("유저2")
            .password("123")
            .role(UserRoleEnum.USER)
            .bookRents(new ArrayList<BookRent>())
            .bookReservations(new ArrayList<BookReservation>())
            .build();
    Book book1 = Book.builder()
            .bookId(1L)
            .bookName("책1")
            .bookAuthor("작가1")
            .bookPublish("2011")
            .bookReservations(new ArrayList<BookReservation>())
            .bookStatus(BookStatusEnum.POSSIBLE)
            .build();
    Book book2 = Book.builder()
            .bookId(2L)
            .bookName("책2")
            .bookAuthor("작가2")
            .bookPublish("2011")
            .bookReservations(new ArrayList<BookReservation>())
            .bookStatus(BookStatusEnum.IMPOSSIBLE)
            .build();
    Book book3 = Book.builder()
            .bookId(3L)
            .bookName("책3")
            .bookAuthor("작가3")
            .bookPublish("2011")
            .bookReservations(new ArrayList<BookReservation>())
            .bookStatus(BookStatusEnum.IMPOSSIBLE)
            .build();

    @Test
    void createRental_성공() {
        //given
        BookRent bookRent1 = BookRent.builder()
                .bookRentId(1L)
                .returnDate(LocalDateTime.parse("2026-08-11T19:16:01"))
                .build();
        //when
        when(bookRepository.findByIdLock(1L)).thenReturn(Optional.of(book1));
        when(userRepository.findByIdFetchBookRent(1L)).thenReturn(Optional.of(user1));
        when(bookRentRepository.save(any(BookRent.class))).thenReturn(bookRent1);
        //then
        MessageDto result = bookRentService.createRental(1L, user1);
        assertThat(result.getMsg()).isEqualTo("도서 대출 신청이 완료되었습니다");
        assertThat(book1.getBookStatus()).isEqualTo(BookStatusEnum.IMPOSSIBLE);
        assertThat(user1.getBookRents()).contains(bookRent1);
    }

    @Test
    void createRental_실패_book없음() {
        //given

        //when
        when(bookRepository.findByIdLock(1L)).thenReturn(Optional.of(book1));
        //then
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                bookRentService.createRental(2L, user1));
        assertThat(e.getMessage()).isEqualTo("book을 찾을 수 없습니다.");
    }

    @Test
    void createRental_실패_대여중인책() {
        //given

        //when
        when(bookRepository.findByIdLock(2L)).thenReturn(Optional.of(book2));
        when(userRepository.findByIdFetchBookRent(1L)).thenReturn(Optional.of(user1));
        //then
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                bookRentService.createRental(2L, user1));
        assertThat(e.getMessage()).isEqualTo("책이 대여 가능한 상태가 아닙니다.");
    }

    @Test
    void deleteRental_성공_예약자없음() {
        //given
        BookRent bookRent1 = BookRent.builder()
                .bookRentId(1L)
                .book(book3)
                .returnDate(LocalDateTime.parse("2026-08-11T19:16:01"))
                .build();
        book3.addBookRent(bookRent1);
        user1.addBookRent(bookRent1);

        //when
        when(bookRepository.findById(3L)).thenReturn(Optional.of(book3));
        when(userRepository.findByIdFetchBookRent(1L)).thenReturn(Optional.of(user1));
        //then
        MessageDto result = bookRentService.deleteRental(3L, user1);
        assertThat(result.getMsg()).isEqualTo("도서 반납이 완료되었습니다");
        assertThat(book3.getBookStatus()).isEqualTo(BookStatusEnum.POSSIBLE);
    }

    @Test
    void deleteRental_성공_예약자있음() {
        //given
        BookRent bookRent1 = BookRent.builder()
                .bookRentId(1L)
                .book(book3)
                .returnDate(LocalDateTime.parse("2026-08-11T19:16:01"))
                .build();
        BookReservation bookReservation = BookReservation.builder()
                .bookReservationId(1L)
                .user(user2)
                .book(book3)
                .build();

        book3.addBookRent(bookRent1);
        user1.addBookRent(bookRent1);

        book3.addBookReservation(bookReservation);
        user2.addBookReservation(bookReservation);

        //when
        when(bookRepository.findById(3L)).thenReturn(Optional.of(book3));
        when(userRepository.findByIdFetchBookRent(1L)).thenReturn(Optional.of(user1));
        when(bookRentRepository.save(any(BookRent.class))).thenReturn(new BookRent(book3));
        //then
        MessageDto result = bookRentService.deleteRental(3L, user1);
        assertThat(result.getMsg()).isEqualTo("도서 반납이 완료되었습니다");
        assertThat(book3.getBookStatus()).isEqualTo(BookStatusEnum.IMPOSSIBLE);
        assertThat(user2.getBookRents().get(0).getBook()).isEqualTo(book3);
    }

    @Test
    void deleteRental_실패_book없음() {
        //given

        //when
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));
        //then
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                bookRentService.deleteRental(2L, user1));
        assertThat(e.getMessage()).isEqualTo("book을 찾을 수 없습니다.");
    }

    @Test
    void deleteRental_실패_Book대여중아님() {
        //given
        BookRent bookRent1 = BookRent.builder()
                .bookRentId(1L)
                .book(book3)
                .returnDate(LocalDateTime.parse("2026-08-11T19:16:01"))
                .build();
        book3.addBookRent(bookRent1);
        user1.addBookRent(bookRent1);

        //when
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));
        when(userRepository.findByIdFetchBookRent(1L)).thenReturn(Optional.of(user1));
        //then
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                bookRentService.deleteRental(1L, user1));
        assertThat(e.getMessage()).isEqualTo("해당 책은 대여중이 아닙니다.");
    }

    @Test
    void deleteRental_실패_Book을대여중아님() {
        //given
        BookRent bookRent1 = BookRent.builder()
                .bookRentId(1L)
                .book(book2)
                .returnDate(LocalDateTime.parse("2026-08-11T19:16:01"))
                .build();
        book2.addBookRent(bookRent1);

        //when
        when(bookRepository.findById(2L)).thenReturn(Optional.of(book2));
        when(userRepository.findByIdFetchBookRent(1L)).thenReturn(Optional.of(user1));
        //then
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                bookRentService.deleteRental(2L, user1));
        assertThat(e.getMessage()).isEqualTo("해당 책을 대여중이 아닙니다.");
    }
}