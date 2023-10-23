package com.example.team258.common.controller.mixedController.admin;


import com.example.team258.common.dto.BookResponseDto;
import com.example.team258.common.dto.BookResponsePageDto;
import com.example.team258.common.dto.DonationV3ServiceResultDto;
import com.example.team258.common.entity.BookStatusEnum;
import com.example.team258.domain.donation.dto.BookDonationEventPageResponseDtoV3;
import com.example.team258.domain.donation.service.BookApplyDonationService;
import com.example.team258.domain.donation.service.BookDonationEventService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class DonationMixedControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookDonationEventService bookDonationEventService;
    @MockBean
    private BookApplyDonationService bookApplyDonationService;

    @Test
    void donationV3() throws Exception {
        // given
        when(bookDonationEventService.donationV3Service(any(int[].class), any(int.class),any(PageRequest.class)))
                .thenReturn(new DonationV3ServiceResultDto(new BookDonationEventPageResponseDtoV3() ,new int[]{},new int[]{}));

        // when
        // then
        mockMvc.perform(get("/admin/donation/v3"))
                .andExpect(status().isOk())
                .andExpect(view().name("/admin/donationV3"));
    }

    @Test
    void bookSettingV3() throws Exception {
        // given
        when(bookApplyDonationService.getDonationBooksV3(any(BookStatusEnum.class),any(PageRequest.class)
                ,any(BookResponseDto.class))).thenReturn(new BookResponsePageDto(new ArrayList<>(),0));

        // when
        // then
        mockMvc.perform(get("/admin/donation/bookSetting/{donationId}/v3",1))
                .andExpect(status().isOk())
                .andExpect(view().name("/admin/bookSettingV2"));
    }
}