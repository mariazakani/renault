package com.microservice.renault.kafka.config;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9097", "port=9097",
    "auto.create.topics.enable=false"}, topics = {"topic-garage"})
public abstract class KafkaBaseTest {

    @Captor
    public ArgumentCaptor<Exception> exceptionArgumentCaptor;

}
