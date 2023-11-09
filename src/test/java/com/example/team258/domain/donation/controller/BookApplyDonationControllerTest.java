package com.example.team258.domain.donation.controller;

import com.example.team258.domain.donation.dto.BookApplyDonationRequestDto;
import com.example.team258.domain.donation.dto.BookApplyDonationResponseDto;
import com.example.team258.common.dto.BookResponseDto;
import com.example.team258.common.dto.MessageDto;
import com.example.team258.common.entity.Book;
import com.example.team258.domain.donation.entity.BookApplyDonation;
import com.example.team258.common.entity.BookStatusEnum;
import com.example.team258.domain.donation.service.BookApplyDonationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc(addFilters = false)
class BookApplyDonationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookApplyDonationService bookApplyDonationService;

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();
//    @Test
//    void createBookApplyDonation() throws Exception {
//        // given
//        MessageDto msg =  MessageDto.builder()
//                .msg("책 나눔 신청이 완료되었습니다.")
//                .build();
//
//        // when
//        when(bookApplyDonationService.createBookApplyDonation(any(BookApplyDonationRequestDto.class))).thenReturn(msg);
//
//        // then
//        mockMvc.perform(post("/api/user/bookApplyDonation")
//                        .contentType("application/json")
//                        .content(objectMapper.writeValueAsString(new BookApplyDonationRequestDto())))
//                .andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("책 나눔 신청이 완료되었습니다."))
//                .andDo(print());
//    }

    @Test
    void deleteBookApplyDonation() throws Exception {
        // given
        MessageDto msg =  MessageDto.builder()
                .msg("책 나눔 신청이 취소되었습니다.")
                .build();

        // when
        when(bookApplyDonationService.deleteBookApplyDonation(any(Long.class))).thenReturn(msg);

        // then
        mockMvc.perform(delete("/api/user/bookApplyDonation/{applyId}", 1L))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("책 나눔 신청이 취소되었습니다."))
                .andDo(print());
    }

    @Test
    void getDonationBooks() throws Exception {
        // given
        List<BookResponseDto> bookResponseDtos = new ArrayList<>();
        Book book = Book.builder()
                .bookId(1L)
                .bookName("bookName")
                .bookAuthor("bookAuthor")
                .bookPublish("2011")
                .bookStatus(BookStatusEnum.POSSIBLE)
                .build();
        bookResponseDtos.add(new BookResponseDto(book));

        // when
        when(bookApplyDonationService.getDonationBooks(any(BookStatusEnum.class))).thenReturn(bookResponseDtos);

        //then
        mockMvc.perform(get("/api/user/bookApplyDonation/books?bookStatus=POSSIBLE"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].bookId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].bookName").value("bookName"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].bookAuthor").value("bookAuthor"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].bookPublish").value("2011"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].bookStatus").value("POSSIBLE"))
                .andDo(print());

    }

    @Test
    void getBookApplyDonations() throws Exception {
        // given
        List<BookApplyDonationResponseDto> bookApplyDonationResponseDtos = new ArrayList<>();
        Book book = Book.builder()
                .bookId(1L)
                .bookName("bookName")
                .bookAuthor("bookAuthor")
                .bookPublish("2011")
                .bookStatus(BookStatusEnum.POSSIBLE)
                .build();
        BookApplyDonation bookApplyDonation = BookApplyDonation.builder()
                .applyId(1L)
                .applyDate(LocalDateTime.parse("2021-08-01T00:00:00"))
                .book(book)
                .build();

        bookApplyDonationResponseDtos.add(new BookApplyDonationResponseDto(bookApplyDonation));

        // when
        when(bookApplyDonationService.getBookApplyDonations()).thenReturn(bookApplyDonationResponseDtos);

        // then
        mockMvc.perform(get("/api/user/bookApplyDonation"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].applyId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].applyDate").value("2021-08-01T00:00:00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].bookId").value(1L))
                .andDo(print());

    }
}