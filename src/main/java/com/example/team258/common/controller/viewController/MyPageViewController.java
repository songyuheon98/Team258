package com.example.team258.common.controller.viewController;

import com.example.team258.common.security.UserDetailsImpl;
import com.example.team258.common.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyPageViewController {
    private UserService userService; // 사용자 정보를 가져오는 서비스

    @GetMapping("/mypage")
    public String mypageView(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        SecurityContext securityContextHolder = SecurityContextHolder.getContext();
        Object principal = securityContextHolder.getAuthentication().getPrincipal();
        String role = "";
        if(principal instanceof UserDetailsImpl)
            role = String.valueOf(((UserDetailsImpl) principal).getUser().getRole());
        else
            role = "ANONYMOUS";

        model.addAttribute("loginUserRole", role);

        // UserDetails에서 필요한 정보 추출
        String loggedInUsername = userDetails.getUsername();

        // 모델에 사용자 정보 추가
        model.addAttribute("loggedInUsername", loggedInUsername);

        return "users/mypage";
    }
}