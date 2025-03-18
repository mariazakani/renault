package com.microservice.renault.kafka.producer;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class KafkaProducer {

    private final ProducerListenerCustom producerListener;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @PostConstruct
    private void addListener() {
        kafkaTemplate.setProducerListener(producerListener);
    }

    public void produceEvent(Object object) {
        kafkaTemplate.send("topic-garage", object);
    }

    public KafkaTemplate<String, Object> getKafkaTemplate() {
        return kafkaTemplate;
    }
}
