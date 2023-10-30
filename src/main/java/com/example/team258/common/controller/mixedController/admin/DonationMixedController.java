package com.example.team258.common.controller.mixedController.admin;

import com.example.team258.common.dto.BookResponseDto;
import com.example.team258.common.dto.BookResponsePageDto;
import com.example.team258.common.dto.DonationV3ServiceResultDto;
import com.example.team258.common.entity.BookStatusEnum;
import com.example.team258.common.repository.BookRepository;
import com.example.team258.domain.donation.service.BookApplyDonationService;
import com.example.team258.domain.donation.service.BookDonationEventService;
import com.example.team258.domain.donation.service.BookService;
import com.example.team258.kafka.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/donation")
@RequiredArgsConstructor
public class DonationMixedController {
    private final BookDonationEventService bookDonationEventService;
    private final BookApplyDonationService bookApplyDonationService;
    private final BookRepository bookRepository;
    private final BookService bookService;
    private final KafkaProducerService producer;

//    @GetMapping
//    public String donation(Model model) {
//        List<BookDonationEventResponseDto> bookDonationEventResponseDtos = bookDonationEventService.getDonationEvent();
//
//        model.addAttribute("events", bookDonationEventResponseDtos);
//        return "/admin/donation";
//    }
//
//    @GetMapping("/v2")
//    public String donationV2(@RequestParam(defaultValue = "0") int page, Model model) {
//        PageRequest pageRequest = PageRequest.of(page, 5);  // page 파라미터로 받은 값을 사용
//
//        BookDonationEventPageResponseDto bookDonationEventPageResponseDtos = bookDonationEventService.getDonationEventV2(pageRequest);
//
//        model.addAttribute("events", bookDonationEventPageResponseDtos.getBookDonationEventResponseDtos());
//        model.addAttribute("currentPage", page);  // 현재 페이지 번호 추가
//        model.addAttribute("totalPages", bookDonationEventPageResponseDtos.getTotalPages());
//        return "/admin/donationV2";
//    }

    /**
     * 관리자 - 책 나눔 이벤트 관리 페이지 v3
     * @param eventPage : 현재 이벤트 페이지
     * @param bookPage :  현재 이벤트에 대한 도서들 페이지
     * @param eventPageSize : 이벤트를 표시할 개수 (페이지)
     * @param bookPageSize : 이벤트에 대한 도서들을 표시할 개수 (페이지)
     * @param model : 모델
     * @return : 뷰
     */
    @GetMapping("/v3")
    public String donationV3(@RequestParam(defaultValue = "0") int eventPage, @RequestParam(defaultValue = "0") int[] bookPage,
                             @RequestParam(defaultValue = "3") int eventPageSize, @RequestParam(defaultValue = "3") int bookPageSize,
                             Model model) {

        PageRequest eventPageRequest = PageRequest.of(eventPage, eventPageSize);  // page 파라미터로 받은 값을 사용
        
        DonationV3ServiceResultDto donationV3ServiceResultDto = bookDonationEventService.donationV3Service(bookPage,  bookPageSize,eventPageRequest);

        model.addAttribute("events", donationV3ServiceResultDto.getBookDonationEventPageResponseDtoV3().getBookDonationEventResponseDtoV3());
        model.addAttribute("currentEventPage", eventPage);  // 현재 페이지 번호 추가
        model.addAttribute("currentBookPage", donationV3ServiceResultDto.getBookPages());  // 현재 페이지 번호 추가
        model.addAttribute("totalPages", donationV3ServiceResultDto.getBookDonationEventPageResponseDtoV3().getTotalPages());
        model.addAttribute("totalBookPages", donationV3ServiceResultDto.getBookPageTotals());

        return "admin/donationV3";
    }

    /**
     * 현재 구현 X
     * @param donationId : 이벤트 아이디
     * @param model : 모델
     * @return : 뷰
     */
//    @GetMapping("/v4")
//    public String donationV4( @RequestParam(defaultValue = "0") int[] bookPage, @RequestParam(defaultValue = "0") int eventPage,
//                              @RequestParam(defaultValue = "0") int eventId, @RequestParam(defaultValue = "") String bookName,
//                              @RequestParam(defaultValue = "") String author, @RequestParam(defaultValue = "") String publish,
//                              @RequestParam(defaultValue = "") String status, Model model) {
//
//        int eventPagesize = 3;
//        int bookPagesize = 3;
//
//        PageRequest eventPageRequest = PageRequest.of(eventPage, eventPagesize);  // page 파라미터로 받은 값을 사용
//        /**
//         * 이벤트 페이징 리스트
//         */
//        BookDonationEventPageResponseDtoV3 bookDonationEventPageResponseDtoV3 =
//                bookDonationEventService.getDonationEventV3(eventPageRequest);
//
//        int eventListSize = bookDonationEventPageResponseDtoV3.getBookDonationEventResponseDtoV3().size();
//        int bookPageTemp[] = new int[eventListSize];
//
//        for (int i = 0; i < bookPage.length; i++)
//            bookPageTemp[i] = bookPage[i];
//
//        /**
//         * 이벤트에 대한 도서들 페이징 리스트
//         */
//        List<Page<Book>> bookPages = new ArrayList<>();
//        for (int i = 0; i < eventListSize; i++) {
//            PageRequest bookPageRequest = PageRequest.of(bookPageTemp[i], bookPagesize);
//            bookPages.add(
//                    bookService.findBookByNameAndRoleAndDonationIdWithPagination(
//                            bookName, author, publish, status,
//                            bookDonationEventPageResponseDtoV3.getBookDonationEventResponseDtoV3().get(i).getDonationId(),
//                            bookPageRequest
//                    ));
//        }
//        /**
//         * 이벤트에 대한 도서들 페이징 리스트를 이벤트에 추가
//         */
//        for (int i = 0; i < bookPages.size(); i++)
//            bookDonationEventPageResponseDtoV3.getBookDonationEventResponseDtoV3().get(i)
//                    .setBookResponseDtos(bookPages.get(i).stream().map(BookResponseDto::new).toList());
//
//
//        model.addAttribute("events", bookDonationEventPageResponseDtoV3.getBookDonationEventResponseDtoV3());
//        model.addAttribute("currentEventPage", eventPage);  // 현재 페이지 번호 추가
//        model.addAttribute("currentBookPage", bookPageTemp);  // 현재 페이지 번호 추가
//        model.addAttribute("totalPages", bookDonationEventPageResponseDtoV3.getTotalPages());
//        model.addAttribute("bookNameInit", bookName);
//        model.addAttribute("authorInit", author);
//        model.addAttribute("publishInit", publish);
//        model.addAttribute("statusInit", status);
//        model.addAttribute("eventIdInit", eventId);
//
//        return "/admin/donationV4";
//    }

//
//    @GetMapping("/bookSetting/{donationId}")
//    public String bookSetting(@PathVariable Long donationId, Model model) {
//        List<BookResponseDto> bookResponseDtos = bookApplyDonationService.getDonationBooks(BookStatusEnum.POSSIBLE);
//
//        model.addAttribute("books", bookResponseDtos);
//        model.addAttribute("donationId", donationId);
//        return "/admin/bookSetting";
//    }
//
//    @GetMapping("/bookSetting/{donationId}/v2")
//    public String bookSettingV2(@RequestParam(defaultValue = "0") int page, @PathVariable Long donationId, Model model) {
//        PageRequest pageRequest = PageRequest.of(page, 15);  // page 파라미터로 받은 값을 사용
//
//        BookResponsePageDto bookResponsePageDto = bookApplyDonationService.getDonationBooksV2(BookStatusEnum.POSSIBLE, pageRequest);
//
//        model.addAttribute("books", bookResponsePageDto.getBookResponseDtos());
//        model.addAttribute("currentPage", page);
//        model.addAttribute("totalPages", bookResponsePageDto.getTotalPages());
//        model.addAttribute("donationId", donationId);
//        return "/admin/bookSettingV2";
//    }


    /**
     * 관리자 - 책 설정 페이지
     * @param page : 현재 페이지
     * @param donationId : 이벤트 아이디
     * @param model : 모델
     * @param bookId : 검색 책 아이디
     * @param bookName : 검색 책 이름
     * @param author : 검색 책 저자
     * @param publish : 검색 책 연도
     * @return : 뷰
     */
    @GetMapping("/bookSetting/{donationId}/v3")
    public String bookSettingV3(@RequestParam(defaultValue = "0") int page, @PathVariable Long donationId, Model model, @RequestParam(defaultValue = "") Long bookId,
                                @RequestParam(defaultValue = "") String bookName, @RequestParam(defaultValue = "") String author,
                                @RequestParam(defaultValue = "") String publish) {

        PageRequest pageRequest = PageRequest.of(page, 15);  // page 파라미터로 받은 값을 사용

        BookResponsePageDto bookResponsePageDto = bookApplyDonationService.getDonationBooksV3(BookStatusEnum.POSSIBLE, pageRequest,new BookResponseDto(bookId,bookName, author, publish));

        model.addAttribute("books", bookResponsePageDto.getBookResponseDtos());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", bookResponsePageDto.getTotalPages());
        model.addAttribute("donationId", donationId);

        return "admin/bookSettingV2";
    }
}