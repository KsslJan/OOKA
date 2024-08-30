package ooka.kessel.fluidanalysisms.kafka;

import ooka.kessel.fluidanalysisms.dto.AnalysisResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
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