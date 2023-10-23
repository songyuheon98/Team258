package com.example.team258.common.controller.mixedController.user;

import com.example.team258.domain.donation.dto.UserBookApplyCancelPageResponseDto;
import com.example.team258.domain.donation.service.BookApplyDonationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users/bookApplyDonation")
@RequiredArgsConstructor
public class BookApplyDonationCancelMixedController {
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
