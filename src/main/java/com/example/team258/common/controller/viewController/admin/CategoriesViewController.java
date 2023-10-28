package com.example.team258.common.controller.viewController.admin;

import com.example.team258.common.dto.BooksCategoryPageResponseDto;
import com.example.team258.common.security.UserDetailsImpl;
import com.example.team258.domain.admin.service.AdminCategoriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class CategoriesViewController {
    private final AdminCategoriesService adminCategoriesService;

    @GetMapping("/admin/categories")
    public String categories( @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, // 현재 페이지, 페이지 크기
                              @RequestParam(defaultValue = "bookCategoryId") String sort, @RequestParam(defaultValue = "ASC") String direction, // 정렬 기준, 정렬 방향
                              @RequestParam(name = "keyword", required = false) String keyword, // 검색 키워드
                              @AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        long startTime = System.currentTimeMillis();//실행시간 측정

        Sort.Direction sortDirection = Sort.Direction.fromString(direction);

        PageRequest pageRequest = PageRequest.of(page, size, sortDirection, sort);

        BooksCategoryPageResponseDto booksCategoryPageResponseDto =
                adminCategoriesService.findBooksCategoriesWithPaginationAndSearching(userDetails.getUser(), keyword, pageRequest);

        model.addAttribute("categories", booksCategoryPageResponseDto);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", booksCategoryPageResponseDto.getTotalPages());
        long endTime = System.currentTimeMillis();
        long durationTimeSec = endTime - startTime;
        System.out.println(durationTimeSec + "m/s"); // 실행시간 측정

        return "admin/categories";
    }
}
