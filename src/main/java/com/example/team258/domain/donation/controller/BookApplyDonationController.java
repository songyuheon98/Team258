package com.example.team258.domain.donation.controller;

import com.example.team258.common.dto.BookResponseDto;
import com.example.team258.common.dto.MessageDto;
import com.example.team258.common.entity.BookStatusEnum;
import com.example.team258.common.jwt.SecurityUtil;
import com.example.team258.domain.donation.dto.BookApplyDonationRequestDto;
import com.example.team258.domain.donation.dto.BookApplyDonationResponseDto;
import com.example.team258.domain.donation.service.BookApplyDonationService;
import com.example.team258.kafka.KafkaProducerService;
import com.example.team258.kafka.dto.MessageKafkaDto;
import com.example.team258.kafka.dto.UserEventApplyKafkaDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.example.team258.common.jwt.JwtUtil.logger;
import static org.apache.kafka.common.requests.FetchMetadata.log;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class BookApplyDonationController {

    private final BookApplyDonationService bookApplyDonationService;
    private final KafkaProducerService producer;
    private final Map<String, CompletableFuture<MessageKafkaDto>> futures = new ConcurrentHashMap<>();
    private ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private final Lock lock=new ReentrantLock();


    @PostMapping("/bookApplyDonation")
    public ResponseEntity<MessageDto> createBookApplyDonation(@RequestBody BookApplyDonationRequestDto bookApplyDonationRequestDto)
            throws JsonProcessingException, ExecutionException, InterruptedException {

        UserEventApplyKafkaDto userEventApplyKafkaDto = new UserEventApplyKafkaDto(bookApplyDonationRequestDto,
                SecurityUtil.getPrincipal().get().getUserId());
        String correlationId = UUID.randomUUID().toString();
        userEventApplyKafkaDto.setCorrelationId(correlationId);

        String jsonString = objectMapper.writeValueAsString(userEventApplyKafkaDto);

        System.out.println("jsonString = " + jsonString);
        CompletableFuture<MessageKafkaDto> future = new CompletableFuture<>();

        futures.put(correlationId, future);

        producer.sendMessage("user-event-apply-input-topic", jsonString);

        MessageKafkaDto messageKafkaDto = future.get(); // 결과를 기다립니다.


        return ResponseEntity.ok().body(messageKafkaDto.getMessageDto());
    }
    @KafkaListener(topics = "user-event-apply-output-topic", groupId = "user-event-apply-consumer-group",
    containerFactory = "kafkaListenerContainerFactory2")
    public void AdminUserManagementConsumer(String message) throws JsonProcessingException {

        MessageKafkaDto messageKafkaDto = objectMapper.readValue(message, MessageKafkaDto.class);

        CompletableFuture<MessageKafkaDto> future = futures.get(messageKafkaDto.getCorrelationId());

        if (future != null) {
            future.complete(messageKafkaDto);
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

