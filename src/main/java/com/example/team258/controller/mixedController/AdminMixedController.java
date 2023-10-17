package com.example.team258.controller.mixedController;

import com.example.team258.dto.UserResponseDto;
import com.example.team258.entity.User;
import com.example.team258.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class AdminMixedController {
    private final UserRepository userRepository;

    @GetMapping("/admin/users")
    public String adminView(Model model) {
        List<User> users = userRepository.findAll();
        List<UserResponseDto> userResponseDtos = users.stream().
                map(UserResponseDto::new).collect(Collectors.toList());

        model.addAttribute("users", userResponseDtos);

        return "/admin/adminUsers";
    }
}