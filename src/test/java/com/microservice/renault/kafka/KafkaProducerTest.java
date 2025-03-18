package com.microservice.renault.kafka;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import com.microservice.renault.dto.GarageDto;
import com.microservice.renault.kafka.producer.KafkaProducer;
import com.microservice.renault.kafka.producer.ProducerListenerCustom;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
class KafkaProducerTest extends com.microservice.renault.kafka.config.KafkaBaseTest {

    @Captor
    ArgumentCaptor<ProducerRecord<String, Object>> producerRecordArgumentCaptor;
    @Captor
    ArgumentCaptor<RecordMetadata> recordMetadataArgumentCaptor;
    @Autowired
    KafkaProducer producer;
    @MockitoSpyBean
    ProducerListenerCustom producerListener;
    String message;

    @BeforeAll
    void setUp() {
        GarageDto garage = new GarageDto();
        garage.setName("Garage A");
        garage.setEmail("contact@renault.com");
        garage.setAddress("Address A");
        message = garage.toString();

    }

    @Test
    @Order(1)
    void embeddedKafkaTestProducer() {
        producer.produceEvent(message);
        verify(producerListener, timeout(1000).times(1)).onSuccess(producerRecordArgumentCaptor.capture(),
            recordMetadataArgumentCaptor.capture());
        ProducerRecord<String, Object> record = producerRecordArgumentCaptor.getValue();
        RecordMetadata recordMetadata = recordMetadataArgumentCaptor.getValue();
        assertNotNull(record);
        assertNotNull(recordMetadata);
        assertEquals("topic-garage", record.topic());
        assertEquals(message, record.value());
    }

    @Test
    @Order(2)
    void embeddedKafkaTestProducerWithError() {
        KafkaTemplate<String, Object> kafkaTemplate = producer.getKafkaTemplate();

        KafkaException thrown = assertThrows(KafkaException.class,
            () -> kafkaTemplate.send("non-existent-topic", message));

        assertTrue(thrown.getCause().getMessage().contains("Topic non-existent-topic not present"));
        verify(producerListener, timeout(1000).times(1)).onError(producerRecordArgumentCaptor.capture(),
            recordMetadataArgumentCaptor.capture(), exceptionArgumentCaptor.capture());
        ProducerRecord<String, Object> record = producerRecordArgumentCaptor.getValue();
        RecordMetadata recordMetadata = recordMetadataArgumentCaptor.getValue();
        Exception exception = exceptionArgumentCaptor.getValue();
        assertNotNull(recordMetadata);
        assertNotNull(record);
        assertNotNull(exception);
    }
}

