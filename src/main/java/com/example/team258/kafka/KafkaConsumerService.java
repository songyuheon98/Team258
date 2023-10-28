package com.example.team258.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
public class KafkaConsumerService {
    @KafkaListener(topics = "test", groupId = "test-consumer-group")
    public void consume(String message){
        System.out.println("Received Message in group 'test-consumer-group': " + message);
    }
}
