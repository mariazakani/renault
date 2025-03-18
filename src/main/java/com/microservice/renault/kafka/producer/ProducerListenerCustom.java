package com.microservice.renault.kafka.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class ProducerListenerCustom implements ProducerListener<String, Object> {

    @Override
    public void onSuccess(ProducerRecord<String, Object> producerRecord, RecordMetadata recordMetadata) {
        String topicName = producerRecord.topic();
        log.info("Message sent to topic: '{}' - with status : SUCCESS", topicName);
    }

    @Override
    public void onError(ProducerRecord<String, Object> producerRecord, RecordMetadata recordMetadata, Exception exception) {
        String topicName = producerRecord.topic();
        log.error("Failed to send message to topic: {} - exception: {}", topicName, exception.getMessage());
    }
}

