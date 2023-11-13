package com.example.team258.common.controller.viewController;

import com.example.team258.common.entity.User;
import com.example.team258.common.jwt.SecurityUtil;
import com.example.team258.common.security.UserDetailsImpl;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.Optional;

@Controller
public class AboutUsViewController {
    @GetMapping("/about258")
    public String aboutUs(Model model) {
        SecurityContext securityContextHolder = SecurityContextHolder.getContext();
        Object principal = securityContextHolder.getAuthentication().getPrincipal();
        String role = "";
        if(principal instanceof UserDetailsImpl)
            role = String.valueOf(((UserDetailsImpl) principal).getUser().getRole());
        else
            role = "ANONYMOUS";

        model.addAttribute("loginUserRole", role);
        return "about258";
    }
}