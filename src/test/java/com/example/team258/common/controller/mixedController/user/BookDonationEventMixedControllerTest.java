package com.example.team258.common.controller.mixedController.user;

import com.example.team258.common.dto.BookApplyDonationEventResultDto;
import com.example.team258.domain.donation.dto.BookDonationEventOnlyPageResponseDto;
import com.example.team258.domain.donation.dto.BookDonationEventResponseDto;
import com.example.team258.domain.donation.service.BookDonationEventService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class BookDonationEventMixedControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    BookDonationEventService bookDonationEventService;


    @Test
    void bookDonation() throws Exception {
        //given
        // when
        when(bookDonationEventService.getDonationEvent()).thenReturn(new ArrayList<>());

        //then
        mockMvc.perform(get("/users/bookDonationEvent"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/bookDonationEvent"));

    }

    @Test
    void bookDonationEventOnlyV3() throws Exception {
        //given
        // when
        when(bookDonationEventService.getDonationEventOnlyV3(any(),any(),any(),any()))
                .thenReturn(BookDonationEventOnlyPageResponseDto.builder().bookDonationEventOnlyResponseDtos(new ArrayList<>()).build());
        //then
        mockMvc.perform(get("/users/bookDonationEvent/v3"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/bookDonationEventV2"));
    }

    @Test
    void bookApplyDonationEventPageV2() throws Exception {
        //given
        // when
        when(bookDonationEventService.bookApplyDonationEventPageV2Result(any(),any())).thenReturn(new BookApplyDonationEventResultDto(new BookDonationEventResponseDto(), new ArrayList<>(),1));

        // then
        mockMvc.perform(get("/users/bookDonationEvent/{donationId}/v2",1L))
                .andExpect(status().isOk())
                .andExpect(view().name("users/bookApplyDonationV2"));
    }
}