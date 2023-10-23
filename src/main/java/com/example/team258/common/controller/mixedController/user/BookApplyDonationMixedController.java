package com.example.team258.common.controller.mixedController.user;

import com.example.team258.common.dto.BookResponseDto;
import com.example.team258.domain.donation.dto.UserBookApplyCancelPageResponseDto;
import com.example.team258.common.entity.BookStatusEnum;
import com.example.team258.domain.donation.repository.BookApplyDonationRepository;
import com.example.team258.common.repository.UserRepository;
import com.example.team258.domain.donation.service.BookApplyDonationService;
import com.example.team258.domain.donation.service.BookDonationEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/users/bookApplyDonation")
@RequiredArgsConstructor
public class BookApplyDonationMixedController {
    private final BookApplyDonationService bookApplyDonationService;
//    private final BookDonationEventService bookDonationEventService;
//    private final BookApplyDonationRepository bookApplyDonationRepository;
//    private final UserRepository userRepository;

    /**
     * 기부 신청 취소 페이지
     * @param model
     * @return
     */
    @GetMapping("/cancel")
    public String bookApplyDonationCancelPage(Model model) {
        UserBookApplyCancelPageResponseDto userBookApplyCancelPageResponseDto = bookApplyDonationService.getDonationBooksCancel();
        model.addAttribute("userBookApplyCancelPageResponseDto", userBookApplyCancelPageResponseDto);
        return "/users/bookApplyDonationCancel";
    }

//    @GetMapping
//    public String bookApplyDonation(Model model) {
//        List<BookResponseDto> bookResponseDtos = bookApplyDonationService.getDonationBooks(BookStatusEnum.DONATION);
//
//        model.addAttribute("books", bookResponseDtos);
//
//        return "/users/bookApplyDonation";
//    }
}
