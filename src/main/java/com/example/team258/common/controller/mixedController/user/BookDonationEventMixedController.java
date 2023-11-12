package com.example.team258.common.controller.mixedController.user;

import com.example.team258.common.dto.BookApplyDonationEventResultDto;
import com.example.team258.common.dto.BookResponseDto;
import com.example.team258.common.entity.Book;
import com.example.team258.common.entity.BookStatusEnum;
import com.example.team258.common.repository.BookRepository;
import com.example.team258.common.security.UserDetailsImpl;
import com.example.team258.domain.donation.dto.BookDonationEventOnlyPageResponseDto;
import com.example.team258.domain.donation.dto.BookDonationEventResponseDto;
import com.example.team258.domain.donation.entity.BookApplyDonation;
import com.example.team258.domain.donation.entity.BookDonationEvent;
import com.example.team258.domain.donation.repository.BookDonationEventRepository;
import com.example.team258.domain.donation.service.BookDonationEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/users/bookDonationEvent")
@RequiredArgsConstructor
public class BookDonationEventMixedController {
    private final BookDonationEventService bookDonationEventService;

    @GetMapping
    public String bookDonation(Model model) {
        List<BookDonationEventResponseDto> bookResponseDtos = bookDonationEventService.getDonationEvent();
        model.addAttribute("events", bookResponseDtos);

        SecurityContext securityContextHolder = SecurityContextHolder.getContext();
        Object principal = securityContextHolder.getAuthentication().getPrincipal();
        String role = "";
        if(principal instanceof UserDetailsImpl)
            role = String.valueOf(((UserDetailsImpl) principal).getUser().getRole());
        else
            role = "ANONYMOUS";

        model.addAttribute("loginUserRole", role);

        return "users/bookDonationEvent";
    }

    /**
     * 유저 - 참여 가능한 이벤트 리스트를 보여주는 컨트롤러
     * @param page : 현재 페이지
     * @param eventId : 이벤트 아이디
     * @param model : 뷰에 전달할 데이터
     * @param eventStartDate : 검색 할 이벤트 시작일
     * @param eventEndDate  : 검색 할 이벤트 종료일
     * @return : 뷰
     */
    @GetMapping("/v3")
    public String bookDonationEventOnlyV3(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "") Long eventId, Model model,
                                          @RequestParam(defaultValue = "") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate eventStartDate,
                                          @RequestParam(defaultValue = "") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate eventEndDate) {
        PageRequest pageRequest = PageRequest.of(page, 8);  // page 파라미터로 받은 값을 사용

        BookDonationEventOnlyPageResponseDto bookDonationEventOnlyPageResponseDto = bookDonationEventService.getDonationEventOnlyV3(pageRequest,eventId,eventStartDate,eventEndDate);



        model.addAttribute("currentPage",page);
        model.addAttribute("totalPages", bookDonationEventOnlyPageResponseDto.getTotalpages());
        model.addAttribute("events", bookDonationEventOnlyPageResponseDto.getBookDonationEventOnlyResponseDtos());

        SecurityContext securityContextHolder = SecurityContextHolder.getContext();
        Object principal = securityContextHolder.getAuthentication().getPrincipal();
        String role = "";
        if(principal instanceof UserDetailsImpl)
            role = String.valueOf(((UserDetailsImpl) principal).getUser().getRole());
        else
            role = "ANONYMOUS";

        model.addAttribute("loginUserRole", role);

        return "users/bookDonationEventV2";
    }


    /**
     * 유저 - 이벤트가 진행되는 페이지를 보여주는 컨트롤러
     * @param page : 현재 페이지
     * @param pageSize : 한 페이지에 보여줄 책의 개수
     * @param model : 뷰에 전달할 데이터
     * @param donationId : 이벤트 아이디
     * @return : 뷰
     */
    @GetMapping("{donationId}/v2")
    public String bookApplyDonationEventPageV2(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int pageSize,
                                               Model model, @PathVariable Long donationId) {
        PageRequest pageRequest = PageRequest.of(page, pageSize);

        BookApplyDonationEventResultDto bookApplyDonationEventResultDto = bookDonationEventService.bookApplyDonationEventPageV2Result(pageRequest, donationId);


        model.addAttribute("bookDonationEvent", bookApplyDonationEventResultDto.getBookDonationEventResponseDto());
        model.addAttribute("books", bookApplyDonationEventResultDto.getBookResponseDtos());
        model.addAttribute("currentPage",page);
        model.addAttribute("totalPages", bookApplyDonationEventResultDto.getTotalPages());

        SecurityContext securityContextHolder = SecurityContextHolder.getContext();
        Object principal = securityContextHolder.getAuthentication().getPrincipal();
        String role = "";
        if(principal instanceof UserDetailsImpl)
            role = String.valueOf(((UserDetailsImpl) principal).getUser().getRole());
        else
            role = "ANONYMOUS";

        model.addAttribute("loginUserRole", role);


        return "users/bookApplyDonationV2";
    }
}