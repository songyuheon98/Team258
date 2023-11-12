package com.example.team258.common.controller.mixedController.admin;

import com.example.team258.common.security.UserDetailsImpl;
import com.example.team258.domain.member.dto.UserResponseDto;
import com.example.team258.common.entity.User;
import com.example.team258.common.repository.UserRepository;
import com.example.team258.common.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class AdminUsersMixedController {
    private final UserRepository userRepository;
    private final UserService userService;

    /**
     * 관리자 - 사용자 관리 페이지
     * @param page : 현재 페이지
     * @param model : 뷰에 전달할 데이터
     * @return : 뷰
     */
    @GetMapping("/admin/users/v2")
    /**
     * @RequestParam(defaultValue = "0") int page : page 파라미터가 없으면 0으로 설정
     */
    public String adminViewV2(@RequestParam(defaultValue = "0") int page, Model model, @RequestParam(defaultValue = "") String userName
            ,@RequestParam(defaultValue = "") String userRole, @RequestParam(defaultValue = "5") int pageSize ) {

        Page<User> users = userService.findUsersByUsernameAndRoleV1(userName, userRole, PageRequest.of(page, pageSize));

        List<UserResponseDto> userResponseDtos = users.stream().map(UserResponseDto::new).collect(Collectors.toList());

        model.addAttribute("currentPage", page);  // 현재 페이지 번호 추가
        model.addAttribute("totalPages", users.getTotalPages());
        model.addAttribute("users", userResponseDtos);

        SecurityContext securityContextHolder = SecurityContextHolder.getContext();
        Object principal = securityContextHolder.getAuthentication().getPrincipal();
        String role = "";
        if(principal instanceof UserDetailsImpl)
            role = String.valueOf(((UserDetailsImpl) principal).getUser().getRole());
        else
            role = "ANONYMOUS";

        model.addAttribute("loginUserRole", role);

        return "adminV2";
    }

}