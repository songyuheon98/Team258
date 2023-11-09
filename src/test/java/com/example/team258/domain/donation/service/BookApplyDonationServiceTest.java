package com.example.team258.domain.donation.service;


import com.example.team258.common.dto.BookResponseDto;
import com.example.team258.common.dto.BookResponsePageDto;
import com.example.team258.common.dto.MessageDto;
import com.example.team258.common.entity.Book;
import com.example.team258.common.entity.BookStatusEnum;
import com.example.team258.common.entity.User;
import com.example.team258.common.entity.UserRoleEnum;
import com.example.team258.common.jwt.SecurityUtil;
import com.example.team258.common.repository.BookRepository;
import com.example.team258.common.repository.UserRepository;
import com.example.team258.domain.donation.dto.BookApplyDonationRequestDto;
import com.example.team258.domain.donation.dto.BookApplyDonationResponseDto;
import com.example.team258.domain.donation.dto.UserBookApplyCancelPageResponseDto;
import com.example.team258.domain.donation.entity.BookApplyDonation;
import com.example.team258.domain.donation.entity.BookDonationEvent;
import com.example.team258.domain.donation.repository.BookApplyDonationRepository;
import com.example.team258.domain.donation.repository.BookDonationEventRepository;
import com.querydsl.core.BooleanBuilder;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookApplyDonationServiceTest {

    @InjectMocks
    private BookApplyDonationService bookApplyDonationService;
    @Mock private BookRepository bookRepository;
    @Mock private BookDonationEventRepository bookDonationEventRepository;
    @Mock private UserRepository userRepository;
    @Mock private BookApplyDonationRepository bookApplyDonationRepository;
    @Mock private SecurityUtil securityUtil;
    private static MockedStatic<SecurityUtil> mockedSecurityUtil;

    @BeforeAll
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockedSecurityUtil = mockStatic(SecurityUtil.class);
    }
    @AfterAll
    void tearDown() {
        mockedSecurityUtil.close();
    }
    @Test
     void createBookApplyDonation_정상_작동() {
        // given
        Book book = Book.builder()
                .bookName("bookName")
                .bookAuthor("bookAuthor")
                .bookPublish("bookPublish")
                .bookStatus(BookStatusEnum.valueOf("POSSIBLE"))
                .build();

        BookDonationEvent bookDonationEvent = BookDonationEvent.builder()
                .createdAt(LocalDateTime.parse("2021-08-01T00:00:00"))
                .closedAt(LocalDateTime.parse("2024-10-01T00:00:00"))
                .donationId(1L)
                .build();

        User user = User.builder()
                .userId(1L)
                .username("username")
                .password("password")
                .role(UserRoleEnum.USER)
                .build();

        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(book));
        when(bookDonationEventRepository.findFetchJoinById(any(Long.class))).thenReturn(Optional.ofNullable(bookDonationEvent));
        when(userRepository.findFetchJoinById(any(Long.class))).thenReturn(Optional.ofNullable(user));
        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(book));
        when(bookDonationEventRepository.findFetchJoinById(any(Long.class))).thenReturn(Optional.ofNullable(bookDonationEvent));
        when(SecurityUtil.getPrincipal()).thenReturn(Optional.ofNullable(user));
        when(userRepository.findFetchJoinById(any(Long.class))).thenReturn(Optional.ofNullable(user));
        when(bookApplyDonationRepository.save(any(BookApplyDonation.class))).thenReturn(BookApplyDonation.builder().build());

        // when
        MessageDto result =bookApplyDonationService.createBookApplyDonation(BookApplyDonationRequestDto.builder()
                .bookId(1L)
                .donationId(1L)
                .build());

        // then
        assertThat(result.getMsg()).isEqualTo("책 나눔 신청이 완료되었습니다.");

    }

    @Test
    void createBookApplyDonation_나눔_신청한_책이_존재하지_않을떄() {
        // given
        Book book = Book.builder()
                .bookName("bookName")
                .bookAuthor("bookAuthor")
                .bookPublish("bookPublish")
                .bookStatus(BookStatusEnum.valueOf("POSSIBLE"))
                .build();

        BookDonationEvent bookDonationEvent = BookDonationEvent.builder()
                .createdAt(LocalDateTime.parse("2021-08-01T00:00:00"))
                .closedAt(LocalDateTime.parse("2024-10-01T00:00:00"))
                .donationId(1L)
                .build();

        User user = User.builder()
                .userId(1L)
                .username("username")
                .password("password")
                .role(UserRoleEnum.USER)
                .build();

        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(book));
        when(bookDonationEventRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(bookDonationEvent));
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(user));
        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(book));
        when(bookDonationEventRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(bookDonationEvent));
        when(SecurityUtil.getPrincipal()).thenReturn(Optional.ofNullable(user));
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(user));
        when(bookApplyDonationRepository.save(any(BookApplyDonation.class))).thenReturn(BookApplyDonation.builder().build());

        // when
        // then
        assertThrows(IllegalArgumentException.class,
                ()->bookApplyDonationService.createBookApplyDonation(BookApplyDonationRequestDto.builder().donationId(1L).build()));
    }

    @Test
    void createBookApplyDonation_누군가_먼저_신청했을_때() {
        // given
        Book book = Book.builder()
                .bookName("bookName")
                .bookAuthor("bookAuthor")
                .bookPublish("bookPublish")
                .bookStatus(BookStatusEnum.valueOf("POSSIBLE"))
                .bookApplyDonation(BookApplyDonation.builder().build())
                .build();

        BookDonationEvent bookDonationEvent = BookDonationEvent.builder()
                .createdAt(LocalDateTime.parse("2021-08-01T00:00:00"))
                .closedAt(LocalDateTime.parse("2024-10-01T00:00:00"))
                .donationId(1L)
                .build();

        User user = User.builder()
                .userId(1L)
                .username("username")
                .password("password")
                .role(UserRoleEnum.USER)
                .build();

        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(book));
        when(bookDonationEventRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(bookDonationEvent));
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(user));
        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(book));
        when(bookDonationEventRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(bookDonationEvent));
        when(SecurityUtil.getPrincipal()).thenReturn(Optional.ofNullable(user));
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(user));
        when(bookApplyDonationRepository.save(any(BookApplyDonation.class))).thenReturn(BookApplyDonation.builder().build());

        // when
        MessageDto result =bookApplyDonationService.createBookApplyDonation(BookApplyDonationRequestDto.builder()
                .bookId(1L)
                .donationId(1L)
                .build());

        // then
        assertThat(result.getMsg()).isEqualTo("이미 누군가 먼저 신청했습니다.");

    }
    @Test
    void createBookApplyDonation_이벤트_시간이_아닐때() {
        // given
        Book book = Book.builder()
                .bookName("bookName")
                .bookAuthor("bookAuthor")
                .bookPublish("bookPublish")
                .bookStatus(BookStatusEnum.valueOf("POSSIBLE"))
                .build();

        BookDonationEvent bookDonationEvent = BookDonationEvent.builder()
                .createdAt(LocalDateTime.parse("2021-08-01T00:00:00"))
                .closedAt(LocalDateTime.parse("2021-10-01T00:00:00"))
                .donationId(1L)
                .build();

        User user = User.builder()
                .userId(1L)
                .username("username")
                .password("password")
                .role(UserRoleEnum.USER)
                .build();

        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(book));
        when(bookDonationEventRepository.findFetchJoinById(any(Long.class))).thenReturn(Optional.ofNullable(bookDonationEvent));
        when(userRepository.findFetchJoinById(any(Long.class))).thenReturn(Optional.ofNullable(user));
        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(book));
        when(bookDonationEventRepository.findFetchJoinById(any(Long.class))).thenReturn(Optional.ofNullable(bookDonationEvent));
        when(SecurityUtil.getPrincipal()).thenReturn(Optional.ofNullable(user));
        when(userRepository.findFetchJoinById(any(Long.class))).thenReturn(Optional.ofNullable(user));
        when(bookApplyDonationRepository.save(any(BookApplyDonation.class))).thenReturn(BookApplyDonation.builder().build());

        // when
        MessageDto result =bookApplyDonationService.createBookApplyDonation(BookApplyDonationRequestDto.builder()
                .bookId(1L)
                .donationId(1L)
                .build());

        // then
        assertThat(result.getMsg()).isEqualTo("책 나눔 이벤트 기간이 아닙니다.");

    }


    @Test
    void deleteBookApplyDonation() {
        // given
        Book book = Book.builder()
                .bookName("bookName")
                .bookAuthor("bookAuthor")
                .bookPublish("bookPublish")
                .bookStatus(BookStatusEnum.valueOf("POSSIBLE"))
                .build();
        BookApplyDonation bookApplyDonation = BookApplyDonation.builder()
                .build();

        bookApplyDonation.addBook(book);

        when(bookApplyDonationRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(bookApplyDonation));
        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(book));
        doNothing().when(bookApplyDonationRepository).delete(any(BookApplyDonation.class));

        // when
        MessageDto result = bookApplyDonationService.deleteBookApplyDonation(1L);

        // then
        assertThat(result.getMsg()).isEqualTo("책 나눔 신청이 취소되었습니다.");
    }

    @Test
    void getDonationBooks() {
        // given
        Book book = Book.builder()
                .bookName("bookName")
                .bookAuthor("bookAuthor")
                .bookPublish("bookPublish")
                .bookStatus(BookStatusEnum.valueOf("POSSIBLE"))
                .build();
        List<Book> books = List.of(book);

        when(bookRepository.findByBookStatus(any(BookStatusEnum.class))).thenReturn(books);

        // when
        List<BookResponseDto> result = bookApplyDonationService.getDonationBooks(BookStatusEnum.POSSIBLE);

        // then
        assertThat(result.get(0).getBookName()).isEqualTo("bookName");
        assertThat(result.get(0).getBookAuthor()).isEqualTo("bookAuthor");
        assertThat(result.get(0).getBookPublish()).isEqualTo("bookPublish");
        assertThat(result.get(0).getBookStatus()).isEqualTo(BookStatusEnum.valueOf("POSSIBLE"));

    }

    @Test
    void getDonationBooksV2() {
        // given
        Book book = Book.builder()
                .bookName("bookName")
                .bookAuthor("bookAuthor")
                .bookPublish("bookPublish")
                .bookStatus(BookStatusEnum.valueOf("POSSIBLE"))
                .build();
        List<Book> books = List.of(book);
        Page<Book> pageBooks = new PageImpl<>(books);

        when(bookRepository.findPageByBookStatus(any(BookStatusEnum.class),any(Pageable.class))).thenReturn(pageBooks);
        when(bookRepository.findPageByBookStatus(any(BookStatusEnum.class),any(Pageable.class))).thenReturn(pageBooks);

        // when
        BookResponsePageDto result = bookApplyDonationService.getDonationBooksV2(BookStatusEnum.POSSIBLE, Pageable.unpaged());

        // then
        assertThat(result.getBookResponseDtos().get(0).getBookName()).isEqualTo("bookName");
        assertThat(result.getBookResponseDtos().get(0).getBookAuthor()).isEqualTo("bookAuthor");
        assertThat(result.getBookResponseDtos().get(0).getBookPublish()).isEqualTo("bookPublish");
        assertThat(result.getBookResponseDtos().get(0).getBookStatus()).isEqualTo(BookStatusEnum.valueOf("POSSIBLE"));
        assertThat(result.getTotalPages()).isEqualTo(1);
    }

    @Test
    void getDonationBooksV3() {
        // given
        Book book = Book.builder()
                .bookName("bookName")
                .bookAuthor("bookAuthor")
                .bookPublish("bookPublish")
                .bookStatus(BookStatusEnum.valueOf("POSSIBLE"))
                .build();
        List<Book> books = List.of(book);
        Page<Book> pageBooks = new PageImpl<>(books);
        PageRequest pageable = PageRequest.of(0, 15);
        BookResponseDto bookResponseDto = new BookResponseDto(1L,"bookName","bookAuthor","bookPublish");

        when(bookRepository.findAll(any(BooleanBuilder.class),any(Pageable.class))).thenReturn(pageBooks);

        // when
        BookResponsePageDto result = bookApplyDonationService.getDonationBooksV3(BookStatusEnum.POSSIBLE, pageable,bookResponseDto);

        // then
        assertThat(result.getBookResponseDtos().get(0).getBookName()).isEqualTo("bookName");
        assertThat(result.getBookResponseDtos().get(0).getBookAuthor()).isEqualTo("bookAuthor");
        assertThat(result.getBookResponseDtos().get(0).getBookPublish()).isEqualTo("bookPublish");
        assertThat(result.getBookResponseDtos().get(0).getBookStatus()).isEqualTo(BookStatusEnum.valueOf("POSSIBLE"));
        assertThat(result.getTotalPages()).isEqualTo(1);
    }

    @Test
    void getBookApplyDonations() {
        // given
        List<BookApplyDonation> bookApplyDonations = List.of(BookApplyDonation.builder()
                        .applyId(1L)
                        .applyDate(LocalDateTime.parse("2021-08-01T00:00:00"))
                        .book(Book.builder().bookId(1L).build())
                        .build()
        );
        when(bookApplyDonationRepository.findAll()).thenReturn(bookApplyDonations);
                // when
        List<BookApplyDonationResponseDto> result = bookApplyDonationService.getBookApplyDonations();

        // then
        assertThat(result.get(0).getApplyId()).isEqualTo(1L);
        assertThat(result.get(0).getApplyDate()).isEqualTo(LocalDateTime.parse("2021-08-01T00:00:00"));

    }

    @Test
    void getDonationBooksCancel() {
        // given
        User user = User.builder()
                .userId(1L)
                .username("username")
                .password("password")
                .role(UserRoleEnum.USER)
                .bookApplyDonations(List.of(BookApplyDonation.builder()
                        .applyId(1L)
                        .applyDate(LocalDateTime.parse("2021-08-01T00:00:00"))
                        .book(Book.builder().bookId(1L).build())
                        .build()))
                .build();

        when(SecurityUtil.getPrincipal()).thenReturn(Optional.ofNullable(user));
        when(userRepository.findFetchJoinById(any(Long.class))).thenReturn(Optional.ofNullable(user));

    // when
        UserBookApplyCancelPageResponseDto result = bookApplyDonationService.getDonationBooksCancel();

        // then
        assertThat(result.getBookResponseDto().get(0).getBookId()).isEqualTo(1L);
        assertThat(result.getUserId()).isEqualTo(1L);
        assertThat(result.getBookApplyId().get(0)).isEqualTo(1L);


    }

}