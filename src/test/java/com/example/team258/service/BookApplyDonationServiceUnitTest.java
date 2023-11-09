package com.example.team258.service;

import com.example.team258.domain.donation.dto.BookApplyDonationRequestDto;
import com.example.team258.domain.donation.dto.BookApplyDonationResponseDto;
import com.example.team258.domain.donation.entity.BookApplyDonation;
import com.example.team258.domain.donation.entity.BookDonationEvent;
import com.example.team258.domain.donation.service.BookApplyDonationService;
import com.example.team258.common.dto.BookResponseDto;
import com.example.team258.common.dto.MessageDto;
import com.example.team258.common.entity.Book;
import com.example.team258.common.entity.BookStatusEnum;
import com.example.team258.common.entity.User;
import com.example.team258.common.entity.UserRoleEnum;
import com.example.team258.common.jwt.SecurityUtil;
import com.example.team258.domain.donation.repository.BookApplyDonationRepository;
import com.example.team258.domain.donation.repository.BookDonationEventRepository;
import com.example.team258.common.repository.BookRepository;
import com.example.team258.common.repository.UserRepository;
import com.example.team258.domain.donation.service.BookApplyDonationService2;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookApplyDonationServiceUnitTest {
    @Mock private BookDonationEventRepository bookDonationEventRepository;
    @Mock private BookApplyDonationRepository bookApplyDonationRepository;
    @Mock private BookApplyDonationService2 bookApplyDonationService2;
    @Mock private BookRepository bookRepository;
    @Mock private UserRepository userRepository;
    @Mock private SecurityUtil securityUtil;
    private BookApplyDonationService bookApplyDonationService;

    private static MockedStatic<SecurityUtil> mockedSecurityUtil;
    @BeforeAll
    void setUp() {
        /**
         * Mockito 어노테이션을 활성화하여 Mock 객체 초기화
         */
        MockitoAnnotations.openMocks(this);

        /**
         * BookApplyDonationService 객체 생성
         */
        bookApplyDonationService = new BookApplyDonationService(bookRepository, bookDonationEventRepository, bookApplyDonationRepository, userRepository,bookApplyDonationService2);

        /**
         * SecurityUtil의 static 메서드를 mock하기 위한 객체 생성
         */
        mockedSecurityUtil = mockStatic(SecurityUtil.class);
    }
    @AfterAll
    void tearDown() {
        mockedSecurityUtil.close();
    }

    @Test
    void createBookApplyDonation() {
        // given
        Book book = Book.builder()
                .bookId(1L)
                .bookName("bookName")
                .bookAuthor("bookAuthor")
                .bookPublish("2011")
                .bookStatus(BookStatusEnum.POSSIBLE)
                .build();

        BookDonationEvent bookDonationEvent = BookDonationEvent.builder()
                .donationId(1L)
                .createdAt(LocalDateTime.parse("2021-07-01T00:00:00"))
                .closedAt(LocalDateTime.parse("2024-09-01T00:00:00"))
                .bookApplyDonations(new ArrayList<>())
                .books(new ArrayList<>())
                .build();

        User user = User.builder()
                .userId(1L)
                .username("username")
                .password("password")
                .role(UserRoleEnum.USER)
                .bookApplyDonations(new ArrayList<>())
                .build();

        BookApplyDonation bookApplyDonation = BookApplyDonation.builder()
                .applyId(1L)
                .applyDate(LocalDateTime.parse("2021-08-01T00:00:00"))
                .build();

        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(book));
        when(bookDonationEventRepository.findFetchJoinById(any(Long.class))).thenReturn(Optional.ofNullable(bookDonationEvent));
        when(SecurityUtil.getPrincipal()).thenReturn(Optional.ofNullable(user));
        when(bookApplyDonationRepository.save(any(BookApplyDonation.class))).thenReturn(bookApplyDonation);
        when(userRepository.findFetchJoinById(any(Long.class))).thenReturn(Optional.ofNullable(user));

        // when
        MessageDto result = bookApplyDonationService.createBookApplyDonation(
                BookApplyDonationRequestDto.builder().bookId(1L).donationId(1L).applyDate(LocalDateTime.parse("2021-08-01T00:00:00")).build());

        //then
        assertThat(result.getMsg()).isEqualTo("책 나눔 신청이 완료되었습니다.");
    }

    @Test
    void createBookApplyDonation_신청한_책이_존재하지_않을_때() {
        // given
        Book book = Book.builder().bookId(1L).bookName("bookName").bookAuthor("bookAuthor").bookPublish("2011")
                .bookStatus(BookStatusEnum.POSSIBLE).build();

        BookDonationEvent bookDonationEvent = BookDonationEvent.builder().donationId(1L).createdAt(LocalDateTime.parse("2021-07-01T00:00:00")).books(new ArrayList<>())
                .closedAt(LocalDateTime.parse("2024-09-01T00:00:00")).bookApplyDonations(new ArrayList<>()).build();

        User user = User.builder().userId(1L).username("username").password("password").role(UserRoleEnum.USER).bookApplyDonations(new ArrayList<>()).build();

        BookApplyDonation bookApplyDonation = BookApplyDonation.builder().applyId(1L).applyDate(LocalDateTime.parse("2021-08-01T00:00:00")).build();

        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        when(bookDonationEventRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(bookDonationEvent));
        when(SecurityUtil.getPrincipal()).thenReturn(Optional.ofNullable(user));
        when(bookApplyDonationRepository.save(any(BookApplyDonation.class))).thenReturn(bookApplyDonation);
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(user));

        // when
        //then
        assertThrows(IllegalArgumentException.class,()->
                bookApplyDonationService.createBookApplyDonation(
                        BookApplyDonationRequestDto.builder().bookId(1L).donationId(1L).applyDate(LocalDateTime.parse("2021-08-01T00:00:00")).build()));
    }

    @Test
    void createBookApplyDonation_이미_누군가_먼저_신청했을때() {
        // given
        Book book = Book.builder().bookId(1L).bookName("bookName").bookAuthor("bookAuthor").bookPublish("2011")
                .bookStatus(BookStatusEnum.POSSIBLE).bookApplyDonation(new BookApplyDonation()).build();

        BookDonationEvent bookDonationEvent = BookDonationEvent.builder().donationId(1L).createdAt(LocalDateTime.parse("2021-07-01T00:00:00")).books(new ArrayList<>())
                .closedAt(LocalDateTime.parse("2024-09-01T00:00:00")).bookApplyDonations(new ArrayList<>()).build();

        User user = User.builder().userId(1L).username("username").password("password").role(UserRoleEnum.USER).bookApplyDonations(new ArrayList<>()).build();

        BookApplyDonation bookApplyDonation = BookApplyDonation.builder().applyId(1L).applyDate(LocalDateTime.parse("2021-08-01T00:00:00")).build();

        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(book));
        when(bookDonationEventRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(bookDonationEvent));
        when(SecurityUtil.getPrincipal()).thenReturn(Optional.ofNullable(user));
        when(bookApplyDonationRepository.save(any(BookApplyDonation.class))).thenReturn(bookApplyDonation);
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(user));

        // when
        MessageDto result = bookApplyDonationService.createBookApplyDonation(
                BookApplyDonationRequestDto.builder().bookId(1L).donationId(1L).applyDate(LocalDateTime.parse("2021-08-01T00:00:00")).build());


        //then
        assertThat(result.getMsg()).isEqualTo("이미 누군가 먼저 신청했습니다.");
    }

    @Test
    void createBookApplyDonation_없는_이벤트를_찾을_때() {
        // given
        Book book = Book.builder().bookId(1L).bookName("bookName").bookAuthor("bookAuthor").bookPublish("2011")
                .bookStatus(BookStatusEnum.POSSIBLE).build();

        BookDonationEvent bookDonationEvent = BookDonationEvent.builder().donationId(1L).createdAt(LocalDateTime.parse("2021-07-01T00:00:00"))
                .closedAt(LocalDateTime.parse("2024-09-01T00:00:00")).bookApplyDonations(new ArrayList<>()).build();

        User user = User.builder().userId(1L).username("username").password("password").role(UserRoleEnum.USER).bookApplyDonations(new ArrayList<>()).build();

        BookApplyDonation bookApplyDonation = BookApplyDonation.builder().applyId(1L).applyDate(LocalDateTime.parse("2021-08-01T00:00:00")).build();

        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(book));
        when(bookDonationEventRepository.findFetchJoinById(any(Long.class))).thenReturn(Optional.empty());
        when(SecurityUtil.getPrincipal()).thenReturn(Optional.ofNullable(user));
        when(bookApplyDonationRepository.save(any(BookApplyDonation.class))).thenReturn(bookApplyDonation);
        when(userRepository.findFetchJoinById(any(Long.class))).thenReturn(Optional.ofNullable(user));

        // when
        //then
        assertThrows(IllegalArgumentException.class,()->
                bookApplyDonationService.createBookApplyDonation(
                        BookApplyDonationRequestDto.builder().bookId(1L).donationId(1L).applyDate(LocalDateTime.parse("2021-08-01T00:00:00")).build()));
    }

    @Test
    void createBookApplyDonation_이벤트_기간이_아님에도_신청했을때() {
        // given
        Book book = Book.builder().bookId(1L).bookName("bookName").bookAuthor("bookAuthor").bookPublish("2011")
                .bookStatus(BookStatusEnum.POSSIBLE).build();

        BookDonationEvent bookDonationEvent = BookDonationEvent.builder().donationId(1L).createdAt(LocalDateTime.parse("2021-07-01T00:00:00"))
                .closedAt(LocalDateTime.parse("2021-09-01T00:00:00")).bookApplyDonations(new ArrayList<>()).build();

        User user = User.builder().userId(1L).username("username").password("password").role(UserRoleEnum.USER).bookApplyDonations(new ArrayList<>()).build();

        BookApplyDonation bookApplyDonation = BookApplyDonation.builder().applyId(1L).applyDate(LocalDateTime.parse("2021-10-01T00:00:00")).build();

        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(book));
        when(bookDonationEventRepository.findFetchJoinById(any(Long.class))).thenReturn(Optional.ofNullable(bookDonationEvent));
        when(SecurityUtil.getPrincipal()).thenReturn(Optional.ofNullable(user));
        when(bookApplyDonationRepository.save(any(BookApplyDonation.class))).thenReturn(bookApplyDonation);
        when(userRepository.findFetchJoinById(any(Long.class))).thenReturn(Optional.ofNullable(user));

        // when
        MessageDto result = bookApplyDonationService.createBookApplyDonation(
                BookApplyDonationRequestDto.builder().bookId(1L).donationId(1L).applyDate(LocalDateTime.parse("2021-10-01T00:00:00")).build());


        //then
        assertThat(result.getMsg()).isEqualTo("책 나눔 이벤트 기간이 아닙니다.");
    }

    @Test
    void createBookApplyDonation_해당_사용자가_도서관_사용자가_아닐_때() {
        // given
        Book book = Book.builder().bookId(1L).bookName("bookName").bookAuthor("bookAuthor").bookPublish("2011")
                .bookStatus(BookStatusEnum.POSSIBLE).build();

        BookDonationEvent bookDonationEvent = BookDonationEvent.builder().donationId(1L).createdAt(LocalDateTime.parse("2021-07-01T00:00:00"))
                .closedAt(LocalDateTime.parse("2024-09-01T00:00:00")).bookApplyDonations(new ArrayList<>()).build();

        User user = User.builder().userId(1L).username("username").password("password").role(UserRoleEnum.USER).bookApplyDonations(new ArrayList<>()).build();

        BookApplyDonation bookApplyDonation = BookApplyDonation.builder().applyId(1L).applyDate(LocalDateTime.parse("2021-10-01T00:00:00")).build();

        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(book));
        when(bookDonationEventRepository.findFetchJoinById(any(Long.class))).thenReturn(Optional.ofNullable(bookDonationEvent));
        when(SecurityUtil.getPrincipal()).thenReturn(Optional.ofNullable(user));
        when(bookApplyDonationRepository.save(any(BookApplyDonation.class))).thenReturn(bookApplyDonation);
        when(userRepository.findFetchJoinById(any(Long.class))).thenReturn(Optional.empty());

        // when
        //then
        assertThrows(IllegalArgumentException.class,()->
                bookApplyDonationService.createBookApplyDonation(
                        BookApplyDonationRequestDto.builder().bookId(1L).donationId(1L).applyDate(LocalDateTime.parse("2021-08-01T00:00:00")).build()));
    }

    @Test
    void deleteBookApplyDonation() {
        // given
        Book book = Book.builder().bookId(1L).bookName("bookName").bookAuthor("bookAuthor").bookPublish("2011")
                .bookStatus(BookStatusEnum.POSSIBLE).build();
        BookApplyDonation bookApplyDonation = BookApplyDonation.builder().applyId(1L)
                .applyDate(LocalDateTime.parse("2021-10-01T00:00:00")).build();

        bookApplyDonation.addBook(book);

        when(bookApplyDonationRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(bookApplyDonation));
        doNothing().when(bookApplyDonationRepository).delete(any(BookApplyDonation.class));

        // when
        MessageDto result = bookApplyDonationService.deleteBookApplyDonation(1L);

        // then
        assertThat(result.getMsg()).isEqualTo("책 나눔 신청이 취소되었습니다.");
    }

    @Test
    void deleteBookApplyDonation_취소할려는_신청이_존재하지_않을때() {
        // given
        Book book = Book.builder().bookId(1L).bookName("bookName").bookAuthor("bookAuthor").bookPublish("2011")
                .bookStatus(BookStatusEnum.POSSIBLE).build();
        BookApplyDonation bookApplyDonation = BookApplyDonation.builder().applyId(1L)
                .applyDate(LocalDateTime.parse("2021-10-01T00:00:00")).build();

        bookApplyDonation.addBook(book);

        when(bookApplyDonationRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        doNothing().when(bookApplyDonationRepository).delete(any(BookApplyDonation.class));

        // when
        // then
        assertThrows(IllegalArgumentException.class,()->
                bookApplyDonationService.deleteBookApplyDonation(1L));
    }

    @Test
    void getDonationBooks() {
        // given
        Book book = Book.builder().bookId(1L).bookName("bookName").bookAuthor("bookAuthor").bookPublish("2011")
                .bookStatus(BookStatusEnum.POSSIBLE).build();
        List<BookResponseDto> bookResponseDtos = new ArrayList<>();
        bookResponseDtos.add(new BookResponseDto(book));

        when(bookRepository.findByBookStatus(any(BookStatusEnum.class))).thenReturn(List.of(book));

        // when
        List<BookResponseDto> result = bookApplyDonationService.getDonationBooks(BookStatusEnum.POSSIBLE);

        // then
        assertThat(result.get(0).getBookId()).isEqualTo(1L);
        assertThat(result.get(0).getBookName()).isEqualTo("bookName");
        assertThat(result.get(0).getBookAuthor()).isEqualTo("bookAuthor");
        assertThat(result.get(0).getBookPublish()).isEqualTo("2011");
        assertThat(result.get(0).getBookStatus()).isEqualTo(BookStatusEnum.POSSIBLE);
    }

    @Test
    void getBookApplyDonations() {
        // given
        Book book = Book.builder().bookId(1L).bookName("bookName").bookAuthor("bookAuthor").bookPublish("2011")
                .bookStatus(BookStatusEnum.POSSIBLE).build();
        BookApplyDonation bookApplyDonation = BookApplyDonation.builder().applyId(1L)
                .applyDate(LocalDateTime.parse("2021-10-01T00:00:00")).build();

        bookApplyDonation.addBook(book);

        List<BookApplyDonation> bookApplyDonations = new ArrayList<>();
        bookApplyDonations.add(bookApplyDonation);

        when(bookApplyDonationRepository.findAll()).thenReturn(bookApplyDonations);

        //when
        List<BookApplyDonationResponseDto> result = bookApplyDonationService.getBookApplyDonations();

        //then
        assertThat(result.get(0).getApplyId()).isEqualTo(1L);
        assertThat(result.get(0).getApplyDate()).isEqualTo("2021-10-01T00:00:00");
    }
}