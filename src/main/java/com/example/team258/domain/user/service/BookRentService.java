package com.example.team258.domain.user.service;

import com.example.team258.domain.user.dto.BookRentResponseDto;
import com.example.team258.domain.user.entity.BookReservation;
import com.example.team258.domain.user.repository.BookRentRepository;
import com.example.team258.domain.user.repository.BookReservationRepository;
import com.example.team258.domain.user.entity.BookRent;
import com.example.team258.common.dto.MessageDto;
import com.example.team258.common.entity.Book;
import com.example.team258.common.entity.BookStatusEnum;
import com.example.team258.common.entity.User;
import com.example.team258.common.repository.BookRepository;
import com.example.team258.common.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookRentService {
    private final BookRentRepository bookRentRepository;
    private final BookReservationRepository bookReservationRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<BookRentResponseDto> getRental(User user) {
        User savedUser = userRepository.findByIdFetchBookRent(user.getUserId())
                .orElseThrow(()->new IllegalArgumentException("user를 찾을 수 없습니다."));
        return savedUser.getBookRents().stream().map(BookRentResponseDto::new).toList();
    }
    @Transactional
    public MessageDto createRental(Long bookId, User user) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(()->new IllegalArgumentException("book을 찾을 수 없습니다."));
        User savedUser = userRepository.findById(user.getUserId())
                .orElseThrow(()->new IllegalArgumentException("user를 찾을 수 없습니다."));
        if (book.getBookStatus() != BookStatusEnum.POSSIBLE) {
            throw new IllegalArgumentException("책이 대여 가능한 상태가 아닙니다.");
        }
        book.changeStatus(BookStatusEnum.IMPOSSIBLE);
        BookRent bookRent = bookRentRepository.save(new BookRent(book));
        book.addBookRent(bookRent);
        savedUser.addBookRent(bookRent);

        return new MessageDto("도서 대출 신청이 완료되었습니다");
    }

    @Transactional
    public MessageDto deleteRental(Long bookId, User user) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(()->new IllegalArgumentException("book을 찾을 수 없습니다."));
        User savedUser = userRepository.findById(user.getUserId())
                .orElseThrow(()->new IllegalArgumentException("user를 찾을 수 없습니다."));
        BookRent bookRent = book.getBookRent();
        if (bookRent == null) {
            throw new IllegalArgumentException("해당 책은 대여중이 아닙니다.");
        }
        if (!savedUser.getBookRents().contains(bookRent)) {
            throw new IllegalArgumentException("해당 책을 대여중이 아닙니다.");
        }

        book.deleteRental();
        bookRentRepository.deleteById(bookRent.getBookRentId()); //확인필요


        //예약자가 있는 경우 첫번째 예약자가 바로 대출
        if (!book.getBookReservations().isEmpty()) {
            User rsvUser = book.getBookReservations().get(0).getUser();
            BookReservation bookReservation = book.getBookReservations().get(0);
            bookReservationRepository.deleteById(bookReservation.getBookReservationId());

            BookRent bookRentRsv = bookRentRepository.save(new BookRent(book));
            book.addBookRent(bookRentRsv);
            rsvUser.addBookRent(bookRentRsv);
        } else {
            book.changeStatus(BookStatusEnum.POSSIBLE);
        }
        return new MessageDto("도서 반납이 완료되었습니다");
    }
}
