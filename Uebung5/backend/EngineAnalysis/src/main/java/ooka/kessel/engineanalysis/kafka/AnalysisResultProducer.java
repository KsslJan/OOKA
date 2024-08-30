package ooka.kessel.engineanalysis.kafka;

import ooka.kessel.engineanalysis.dto.AnalysisResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class AnalysisResultProducer {
    private final KafkaTemplate<String, AnalysisResult> kafkaTemplate;

    @Autowired
    public AnalysisResultProducer(KafkaTemplate<String, AnalysisResult> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendAnalysisResult(String topic, AnalysisResult result) {
        kafkaTemplate.send(topic, result);
    }
}