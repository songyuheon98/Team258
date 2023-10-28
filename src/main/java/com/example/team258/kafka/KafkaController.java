package com.example.team258.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KafkaController {
    private final KafkaProducerService producer;

    @PostMapping("/send")
    public void send(@RequestParam("message") String message){
        producer.sendMessage("test",message);
    }
}
