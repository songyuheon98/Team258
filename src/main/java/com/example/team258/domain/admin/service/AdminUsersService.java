package com.example.team258.domain.admin.service;

import com.example.team258.common.dto.MessageDto;
import com.example.team258.common.entity.Book;
import com.example.team258.common.entity.BookStatusEnum;
import com.example.team258.common.entity.User;
import com.example.team258.common.entity.UserRoleEnum;
import com.example.team258.common.repository.UserRepository;
import com.example.team258.domain.admin.dto.AdminUsersResponseDto;
import com.example.team258.domain.donation.entity.BookApplyDonation;
import com.example.team258.domain.user.entity.BookRent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUsersService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<AdminUsersResponseDto> getAllUsers() {
        return userRepository.findAll().stream().map(AdminUsersResponseDto::new).toList();
    }

    @Transactional
    public MessageDto deleteUser(Long userId, User loginUser) {
        User user = getUserById(userId);

        if(loginUser.getRole().equals(UserRoleEnum.USER)) {
            throw new IllegalArgumentException("관리자가 아닙니다.");
        }


        if (loginUser.getUserId().equals(user.getUserId()) ) {
            throw new IllegalArgumentException("자기 자신은 삭제할 수 없습니다.");
        }
//
        if (user.getBookRents().size() != 0) {
            throw new IllegalArgumentException("대여중인 책이 있는 유저는 삭제할 수 없습니다.");
        }

        //대여기록 삭제를 위한 Book에서의 연관관계 삭제
        List<BookRent> bookRents = user.getBookRents();

        for (BookRent bookRent : bookRents) {
            bookRent.getBook().deleteRental();
        }

        int bookApplyDonationSize = user.getBookApplyDonations().size();
        int reservationsSize = user.getBookReservations().size();
        int rentSize =user.getBookRents().size();
        for (int i = 0; i < bookApplyDonationSize; i++) {
            BookApplyDonation bookApplyDonation =  user.getBookApplyDonations().get(i);
            Book book =bookApplyDonation.getBook();
            book.changeStatus(BookStatusEnum.DONATION);
            bookApplyDonation.removeBook(book);
        }

        userRepository.delete(user);
        return new MessageDto("삭제가 완료되었습니다");
    }


    private User getUserById(Long userId) {
//        User user = userRepository.findFetchJoinById(userId)
//        User user = userRepository.findById(userId)
//        User user = userRepository.findJPQLById(userId)
        User user = userRepository.findFetchJoinBookById(userId)
                .orElseThrow(()-> new IllegalArgumentException("회원을 찾을 수 없습니다."));
        return user;
    }
}