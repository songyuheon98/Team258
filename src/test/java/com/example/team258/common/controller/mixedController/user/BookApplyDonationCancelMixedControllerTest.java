package com.example.team258.common.controller.mixedController.user;

import com.example.team258.domain.donation.dto.UserBookApplyCancelPageResponseDto;
import com.example.team258.domain.donation.service.BookApplyDonationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class BookApplyDonationCancelMixedControllerTest {


    @Autowired
    MockMvc mockMvc;
    @MockBean
    BookApplyDonationService bookApplyDonationService;

    @Test
    @DisplayName("유저 책 이벤트 신청 취소 페이지 테스트")
    void bookApplyDonationCancelPage() throws Exception {
        // given
        Authentication auth = new UsernamePasswordAuthenticationToken("user", "password", Collections.emptyList());
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(auth);
        SecurityContextHolder.setContext(securityContext);

        // when
        when(bookApplyDonationService.getDonationBooksCancel()).thenReturn(new UserBookApplyCancelPageResponseDto());


        // then
        mockMvc.perform(get("/users/bookApplyDonation/cancel"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/bookApplyDonationCancel"));
    }
}