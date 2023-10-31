package com.example.team258.kafka;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {
    // 첫 번째 컨슈머 팩토리를 정의합니다. 이 팩토리는 컨슈머 인스턴스를 생성할 때 사용됩니다.
//    @Bean
//    public ConsumerFactory<String, String> consumerFactory1() {
//        // 카프카 컨슈머의 설정값을 담을 Map을 생성합니다.
//        Map<String, Object> config = new HashMap<>();
//        // 카프카 서버의 주소를 설정합니다.
//        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//        // 컨슈머 그룹의 ID를 설정합니다.
//        config.put(ConsumerConfig.GROUP_ID_CONFIG, "user-management-output-consumer-group");
//        // 메시지의 키와 값에 사용할 직렬화 클래스를 설정합니다.
//        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//
//        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
//
//        // 설정값을 사용해 DefaultKafkaConsumerFactory 인스턴스를 생성하여 반환합니다.
//        return new DefaultKafkaConsumerFactory<>(config);
//    }
    @Bean
    public ConsumerFactory<String, String> consumerFactory2() {
        // 카프카 컨슈머의 설정값을 담을 Map을 생성합니다.
        Map<String, Object> config = new HashMap<>();
        // 카프카 서버의 주소를 설정합니다.
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "43.201.58.31:9092");
        // 컨슈머 그룹의 ID를 설정합니다.
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "user-event-apply-consumer-group");
        // 메시지의 키와 값에 사용할 직렬화 클래스를 설정합니다.
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // 설정값을 사용해 DefaultKafkaConsumerFactory 인스턴스를 생성하여 반환합니다.
        return new DefaultKafkaConsumerFactory<>(config);
    }

    /**
     * KafkaListenerContainerFactory
     * KafkaListenerContainerFactory는 KafkaListenerContainer를 생성하는 팩토리입니다.
     * @KafkaListener 어노테이션을 사용해 메시지를 수신하는 리스너 컨테이너를 생성할 때 사용됩니다.
     * @return
     */
    // 첫 번째 컨슈머 팩토리를 사용하는 리스너 컨테이너 팩토리를 정의합니다.
//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory1() {
//        // ConcurrentKafkaListenerContainerFactory 인스턴스를 생성합니다.
//        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
//        // 앞서 정의한 컨슈머 팩토리를 설정합니다.
//        factory.setConsumerFactory(consumerFactory1());
//        // 팩토리 인스턴스를 반환합니다.
//        return factory;
//    }
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory2() {
        // ConcurrentKafkaListenerContainerFactory 인스턴스를 생성합니다.
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        // 앞서 정의한 컨슈머 팩토리를 설정합니다.
        factory.setConsumerFactory(consumerFactory2());
        // 팩토리 인스턴스를 반환합니다.
        return factory;
    }

    @Bean
    public ProducerFactory<String, String> producerFactory2() {
        // 카프카 프로듀서의 설정값을 담을 Map을 생성합니다.
        Map<String, Object> config = new HashMap<>();
        // 카프카 서버의 주소를 설정합니다.
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "43.201.58.31:9092");
        // 메시지의 키와 값에 사용할 직렬화 클래스를 설정합니다.
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        // 설정값을 사용해 DefaultKafkaProducerFactory 인스턴스를 생성하여 반환합니다.
        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate1() {
        return new KafkaTemplate<>(producerFactory2());
    }
}


