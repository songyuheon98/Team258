package com.example.team258.domain.member.controller;

import com.example.team258.domain.member.dto.UserSignupRequestDto;
import com.example.team258.common.dto.MessageDto;
import com.example.team258.domain.member.dto.UserUpdateRequestDto;
import com.example.team258.common.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<MessageDto> signup(@Valid @RequestBody UserSignupRequestDto requestDto){
        return userService.signup(requestDto);
    }

    @DeleteMapping("/escape")
    public ResponseEntity<MessageDto> escape(){
        ResponseEntity<MessageDto> escape_result= userService.escape();
        return escape_result;

    }

    @PutMapping("/update")
    public ResponseEntity<MessageDto> update(@Valid @RequestBody UserUpdateRequestDto requestDto){
        return ResponseEntity.ok().body(userService.update(requestDto));
    }
}
