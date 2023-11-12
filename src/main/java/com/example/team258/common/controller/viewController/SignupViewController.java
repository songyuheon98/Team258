package com.example.team258.common.controller.viewController;

import com.example.team258.common.dto.MessageDto;
import com.example.team258.common.security.UserDetailsImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Controller
public class SignupViewController {
    @GetMapping("/signup")
    public String signupView(Model model) {
        SecurityContext securityContextHolder = SecurityContextHolder.getContext();
        Object principal = securityContextHolder.getAuthentication().getPrincipal();
        String role = "";
        if(principal instanceof UserDetailsImpl)
            role = String.valueOf(((UserDetailsImpl) principal).getUser().getRole());
        else
            role = "ANONYMOUS";

        model.addAttribute("loginUserRole", role);
        return "signup";
    }

    @GetMapping("/logoutView")
    public String logoutView(Model model){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = attributes.getResponse();

        /**
         * 쿠키 삭제 기능 추가
         */
        Cookie cookie = new Cookie("Authorization", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        return "redirect:/";
    }

}