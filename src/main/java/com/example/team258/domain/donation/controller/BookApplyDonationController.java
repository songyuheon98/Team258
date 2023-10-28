package com.example.team258.domain.donation.controller;

import com.example.team258.common.dto.BookResponseDto;
import com.example.team258.common.dto.MessageDto;
import com.example.team258.common.entity.BookStatusEnum;
import com.example.team258.domain.donation.dto.BookApplyDonationRequestDto;
import com.example.team258.domain.donation.dto.BookApplyDonationResponseDto;
import com.example.team258.domain.donation.service.BookApplyDonationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class BookApplyDonationController {

    private final BookApplyDonationService bookApplyDonationService;
    private final Lock lock=new ReentrantLock();

    @PostMapping("/bookApplyDonation")
    public ResponseEntity<MessageDto> createBookApplyDonation(@RequestBody BookApplyDonationRequestDto bookApplyDonationRequestDto){
        try{
            lock.lock();
            return ResponseEntity.ok().body(bookApplyDonationService.createBookApplyDonation(bookApplyDonationRequestDto));
        }
        finally {
            lock.unlock();
        }
    }

    //낙관적락,비관적락 사용시
    @PostMapping("/bookApplyDonation/v2")
    public ResponseEntity<MessageDto> createBookApplyDonationV2(@RequestBody BookApplyDonationRequestDto bookApplyDonationRequestDto){
        return ResponseEntity.ok().body(bookApplyDonationService.createBookApplyDonationV2(bookApplyDonationRequestDto));
    }

    //Transactional SERIALIZable 사용
    @PostMapping("/bookApplyDonation/v3")
    public ResponseEntity<MessageDto> createBookApplyDonationV3(@RequestBody BookApplyDonationRequestDto bookApplyDonationRequestDto){
        return ResponseEntity.ok().body(bookApplyDonationService.createBookApplyDonationV3(bookApplyDonationRequestDto));
    }

    //redisson 분산락 사용
    @PostMapping("/bookApplyDonation/v4")
    public ResponseEntity<MessageDto> createBookApplyDonationV4(@RequestBody BookApplyDonationRequestDto bookApplyDonationRequestDto){
        return ResponseEntity.ok().body(bookApplyDonationService.createBookApplyDonationV4(bookApplyDonationRequestDto));
    }


    @DeleteMapping("/bookApplyDonation/{applyId}")
    public ResponseEntity<MessageDto> deleteBookApplyDonation(@PathVariable Long applyId){

        return ResponseEntity.ok().body(bookApplyDonationService.deleteBookApplyDonation(applyId));
    }

    /**
     * 책 기부 신청 목록 조회
     * @param bookStatus
     * @return
     */
    @GetMapping("/bookApplyDonation/books")
    public ResponseEntity<List<BookResponseDto>> getDonationBooks(@RequestParam BookStatusEnum bookStatus){
        return ResponseEntity.ok().body(bookApplyDonationService.getDonationBooks(bookStatus));
    }

    @GetMapping("/bookApplyDonation")
    public ResponseEntity<List<BookApplyDonationResponseDto>> getBookApplyDonations(){
        return ResponseEntity.ok().body(bookApplyDonationService.getBookApplyDonations());
    }

}

