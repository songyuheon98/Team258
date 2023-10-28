package com.example.team258.domain.user.service;

//import com.example.team258.common.redisson.DistributeLock;
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
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookRentService {
    private final BookRentRepository bookRentRepository;
    private final BookReservationRepository bookReservationRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
//    private final RedissonClient redissonClient;

    @Transactional(readOnly = true)
    public List<BookRentResponseDto> getRental(User user) {
        User savedUser = userRepository.findByIdFetchBookRent(user.getUserId())
                .orElseThrow(()->new IllegalArgumentException("user를 찾을 수 없습니다."));
        return savedUser.getBookRents().stream().map(BookRentResponseDto::new).toList();
    }

    /**
     * RedissonConfig를 활성화할 경우 현재 github action의 테스트코드 실행시 redis를 실행하지 못해 오류가 발생함
     * 따라서 redisson 분산락을 사용하는 아래의 코드 일부와 commom-config-RedissonConfig 파일 전문을 주석처리한 상태임
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public MessageDto createRental(Long bookId, User user) {
//        RLock lock = redissonClient.getLock(String.valueOf(bookId));
//
//        try {
//            if (!lock.tryLock(3, 3, TimeUnit.SECONDS)) {
//                log.info("락 획득 실패");
//                throw new IllegalArgumentException("락 획득 실패");
//            }
//            log.info("락 획득 성공");

            //로직
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(()->new IllegalArgumentException("book을 찾을 수 없습니다."));
            User savedUser = userRepository.findByIdFetchBookRent(user.getUserId())
                    .orElseThrow(()->new IllegalArgumentException("user를 찾을 수 없습니다."));
            if (book.getBookStatus() != BookStatusEnum.POSSIBLE) {
                throw new IllegalArgumentException("책이 대여 가능한 상태가 아닙니다.");
            }
            book.changeStatus(BookStatusEnum.IMPOSSIBLE);
            BookRent bookRent = bookRentRepository.save(new BookRent(book));
            book.addBookRent(bookRent);
            savedUser.addBookRent(bookRent);
            //로직

//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//            e.printStackTrace();
//        } finally {
//            log.info("finally문 실행");
//            if (lock != null && lock.isLocked() && lock.isHeldByCurrentThread()) {
//                lock.unlock();
//                log.info("언락 실행");
//            }
//        }
        return new MessageDto("도서 대출 신청이 완료되었습니다");

    }

    @Transactional
    public MessageDto deleteRental(Long bookId, User user) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(()->new IllegalArgumentException("book을 찾을 수 없습니다."));
        User savedUser = userRepository.findByIdFetchBookRent(user.getUserId())
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
