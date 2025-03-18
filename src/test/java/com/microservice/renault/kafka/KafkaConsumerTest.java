package com.microservice.renault.kafka;

import com.microservice.renault.dto.GarageDto;
import com.microservice.renault.kafka.consumer.KafkaConsumer;
import com.microservice.renault.kafka.producer.KafkaProducer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;


/**
 *        Work in progress
 */
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@SpringBootTest
//@EmbeddedKafka(partitions = 1, topics = "topic-garage")
public class KafkaConsumerTest /*extends KafkaBaseTest */{

    @Captor
    ArgumentCaptor<Object> valueArgumentCaptor;
    @MockitoSpyBean
    KafkaConsumer consumer;
    @Autowired
    KafkaProducer producer;
    String message;

    //@BeforeAll
    void setUp() {
        GarageDto garage = new GarageDto();
        garage.setName("Garage A");
        garage.setEmail("contact@renault.com");
        garage.setAddress("Address A");
        message = garage.toString();
    }
    //@Test
    void receiveMessageKafkaListenerTest() {
        producer.produceEvent(message);
        verify(consumer, timeout(2000).times(1)).receive(valueArgumentCaptor.capture());
        Object messageValue = valueArgumentCaptor.getValue();
        assertNotNull(messageValue);
        assertEquals(message, messageValue.toString());
    }

}
