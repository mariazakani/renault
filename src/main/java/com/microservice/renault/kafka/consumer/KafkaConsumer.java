package com.microservice.renault.kafka.consumer;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Data
public class KafkaConsumer {
    /**
     *   skip Listener to start application
     */
//    @KafkaListener(topics = "topic-garage", groupId = "group-kafka",
//            containerFactory = "kafkaListenerContainerFactory")
    public void receive(Object message) {
        log.info("received message ='{}'", message.toString());
    }

}
