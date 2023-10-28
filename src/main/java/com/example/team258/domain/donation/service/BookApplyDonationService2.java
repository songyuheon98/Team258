package com.example.team258.domain.donation.service;

import com.example.team258.common.entity.Book;
import com.example.team258.common.entity.BookStatusEnum;
import com.example.team258.common.entity.User;
import com.example.team258.common.jwt.SecurityUtil;
import com.example.team258.common.repository.BookRepository;
import com.example.team258.common.repository.UserRepository;
import com.example.team258.domain.donation.dto.BookApplyDonationRequestDto;
import com.example.team258.domain.donation.entity.BookApplyDonation;
import com.example.team258.domain.donation.entity.BookDonationEvent;
import com.example.team258.domain.donation.repository.BookApplyDonationRepository;
import com.example.team258.domain.donation.repository.BookDonationEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class BookApplyDonationService2 {
    private final BookRepository bookRepository;
    private final BookDonationEventRepository bookDonationEventRepository;
    private final BookApplyDonationRepository bookApplyDonationRepository;
    private final UserRepository userRepository;


    @Transactional
    public void createBookApplyDonation(BookApplyDonationRequestDto bookApplyDonationRequestDto) {
        Book book = bookRepository.findById(bookApplyDonationRequestDto.getBookId())
                .orElseThrow(() -> new IllegalArgumentException("나눔 신청한 책이 존재하지 않습니다."));
        if (book.getBookApplyDonation() != null) {
            throw new IllegalArgumentException("이미 누군가 먼저 신청했습니다.");
        }
        BookDonationEvent bookDonationEvent = bookDonationEventRepository.findById(bookApplyDonationRequestDto.getDonationId())
                .orElseThrow(() -> new IllegalArgumentException("해당 이벤트가 존재하지 않습니다."));

        if (LocalDateTime.now().isBefore(bookDonationEvent.getCreatedAt()) ||
                LocalDateTime.now().isAfter(bookDonationEvent.getClosedAt())) {
            throw new IllegalArgumentException("책 나눔 이벤트 기간이 아닙니다.");
        }
        User user = userRepository.findById(SecurityUtil.getPrincipal().get().getUserId()).orElseThrow(
                () -> new IllegalArgumentException("해당 사용자는 도서관 사용자가 아닙니다.")
        );
        BookApplyDonation bookApplyDonation = new BookApplyDonation(bookApplyDonationRequestDto);
        bookApplyDonationRepository.save(bookApplyDonation);
        bookApplyDonation.addBook(book);
        user.getBookApplyDonations().add(bookApplyDonation);
        bookDonationEvent.getBookApplyDonations().add(bookApplyDonation);
        book.changeStatus(BookStatusEnum.SOLD_OUT);
    }
}
