package com.example.team258.domain.user.service;

import com.example.team258.common.entity.Book;
import com.example.team258.common.entity.BookStatusEnum;
import com.example.team258.common.entity.User;
import com.example.team258.common.repository.BookRepository;
import com.example.team258.common.repository.UserRepository;
import com.example.team258.domain.user.entity.BookRent;
import com.example.team258.domain.user.repository.BookRentRepository;
import com.example.team258.domain.user.repository.BookReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookRentService2 {
    private final BookRentRepository bookRentRepository;
    private final BookReservationRepository bookReservationRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
//    private final RedissonClient redissonClient;


    @Transactional
    public void createRental(Long bookId, User user) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("book을 찾을 수 없습니다."));
        User savedUser = userRepository.findByIdFetchBookRent(user.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("user를 찾을 수 없습니다."));
        if (book.getBookStatus() != BookStatusEnum.POSSIBLE) {
            throw new IllegalArgumentException("책이 대여 가능한 상태가 아닙니다.");
        }
        book.changeStatus(BookStatusEnum.IMPOSSIBLE);
        BookRent bookRent = bookRentRepository.save(new BookRent(book));
        book.addBookRent(bookRent);
        savedUser.addBookRent(bookRent);
    }
}
