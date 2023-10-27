package com.example.team258.common.controller.viewController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyRentalsViewController {
    @GetMapping("/myrentals")
    public String myrentalsView() {
        return "users/myrentals";
    }
}

