package ooka.kessel.engineanalysis.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class AnalysisResultProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public AnalysisResultProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendAnalysisResult(String topic, String key, String result) {
        kafkaTemplate.send(
                MessageBuilder
                        .withPayload(result)
                        .setHeader(KafkaHeaders.TOPIC, topic)
                        .setHeader(KafkaHeaders.KEY, key)
                        .build());
    }
}