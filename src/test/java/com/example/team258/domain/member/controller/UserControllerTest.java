package com.example.team258.domain.member.controller;

import com.example.team258.common.dto.MessageDto;
import com.example.team258.common.service.UserService;
import com.example.team258.domain.member.dto.UserSignupRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Test
    @DisplayName("회원가입 테스트")
    void 사용자_회원가입_테스트() throws Exception {
        // given
        UserSignupRequestDto requestDto = UserSignupRequestDto.builder()
                .username("bin0222")
                .password1("Bin@12345")
                .password2("Bin@12345")
                .build();

        MessageDto msg =  MessageDto.builder()
                .msg("회원가입 성공")
                .build();

        when(userService.signup(any(UserSignupRequestDto.class))).thenReturn(new ResponseEntity<>(msg, HttpStatus.OK));

        // when
        // then
        mockMvc.perform(post("/api/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("회원가입 성공"));
    }

    @Test
    @DisplayName("회원탈퇴 테스트")
    void escape() throws Exception {
        MessageDto msg =  MessageDto.builder()
                .msg("회원 삭제 성공")
                .build();

        when(userService.escape()).thenReturn(new ResponseEntity<>(msg, HttpStatus.OK));

        mockMvc.perform(delete("/api/users/escape"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("회원 삭제 성공"));
    }
}