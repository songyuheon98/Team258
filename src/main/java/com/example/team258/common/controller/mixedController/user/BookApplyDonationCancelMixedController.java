package com.example.team258.common.controller.mixedController.user;

import com.example.team258.common.dto.BookResponseDto;
import com.example.team258.common.entity.BookStatusEnum;
import com.example.team258.common.security.UserDetailsImpl;
import com.example.team258.domain.donation.dto.UserBookApplyCancelPageResponseDto;
import com.example.team258.domain.donation.service.BookApplyDonationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/users/bookApplyDonation")
@RequiredArgsConstructor
public class BookApplyDonationCancelMixedController {
    private final BookApplyDonationService bookApplyDonationService;
    /**
     * 기부 신청 취소 페이지
     * @param model
     * @return
     */
    @GetMapping("/cancel")
    public String bookApplyDonationCancelPage(Model model) {
        UserBookApplyCancelPageResponseDto userBookApplyCancelPageResponseDto = bookApplyDonationService.getDonationBooksCancel();
        model.addAttribute("userBookApplyCancelPageResponseDto", userBookApplyCancelPageResponseDto);

        SecurityContext securityContextHolder = SecurityContextHolder.getContext();
        Object principal = securityContextHolder.getAuthentication().getPrincipal();
        String role = "";
        if(principal instanceof UserDetailsImpl)
            role = String.valueOf(((UserDetailsImpl) principal).getUser().getRole());
        else
            role = "ANONYMOUS";

        model.addAttribute("loginUserRole", role);

        return "users/bookApplyDonationCancel";
    }

    @GetMapping
    public String bookApplyDonation(Model model) {
        List<BookResponseDto> bookResponseDtos = bookApplyDonationService.getDonationBooks(BookStatusEnum.DONATION);

        model.addAttribute("books", bookResponseDtos);

        SecurityContext securityContextHolder = SecurityContextHolder.getContext();
        Object principal = securityContextHolder.getAuthentication().getPrincipal();
        String role = "";
        if(principal instanceof UserDetailsImpl)
            role = String.valueOf(((UserDetailsImpl) principal).getUser().getRole());
        else
            role = "ANONYMOUS";

        model.addAttribute("loginUserRole", role);

        return "users/bookApplyDonation";
    }
}
