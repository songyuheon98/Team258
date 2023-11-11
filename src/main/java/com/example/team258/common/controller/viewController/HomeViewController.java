package com.example.team258.common.controller.viewController;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeViewController {

    @GetMapping("/")
    public String homeView() {
        return "users/searchIS1";
    }
}
