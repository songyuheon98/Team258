package com.example.team258.common.controller.viewController.admin;

import com.example.team258.common.dto.BooksPageResponseDto;
import com.example.team258.domain.admin.dto.AdminBooksResponseDto;
import com.example.team258.common.security.UserDetailsImpl;
import com.example.team258.domain.admin.service.AdminBooksService;
import com.example.team258.domain.admin.service.AdminCategoriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BooksViewController {
    private final AdminCategoriesService adminCategoriesService;
    private final AdminBooksService adminBooksService;

    @GetMapping("/admin/books")
    public String adminBooksView(Model model) {
        // 카테고리 정보를 DB에서 가져와서 모델에 추가
        model.addAttribute("categories", adminCategoriesService.getAllCategories());

        return "admin/books";
    }

    @GetMapping("/admin/booksManage")
    public String adminBooksManageView( @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, // 현재 페이지, 페이지 크기
                                        @RequestParam(defaultValue = "bookId") String sort, @RequestParam(defaultValue = "ASC") String direction, // 정렬 기준, 정렬 방향
                                        @RequestParam(value = "keyword", required = false) String keyword, // 검색 키워드
                                        @AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        long startTime = System.currentTimeMillis();//실행시간 측정

        Sort.Direction sortDirection = Sort.Direction.fromString(direction);

        PageRequest pageRequest = PageRequest.of(page, size, sortDirection, sort);

        BooksPageResponseDto booksPageResponseDto =
                adminBooksService.findBooksWithPaginationAndSearching(userDetails.getUser(), keyword, pageRequest);

        model.addAttribute("books", booksPageResponseDto.getAdminBooksResponseDtos());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", booksPageResponseDto.getTotalPages());


        long endTime = System.currentTimeMillis();
        long durationTimeSec = endTime - startTime;
        System.out.println(durationTimeSec + "m/s"); // 실행시간 측정

        return "admin/booksManage";

    }
}