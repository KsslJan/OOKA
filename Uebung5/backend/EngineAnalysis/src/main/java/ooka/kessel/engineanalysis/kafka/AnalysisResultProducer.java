package ooka.kessel.engineanalysis.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class AnalysisResultProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public AnalysisResultProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendAnalysisResult(String topic, String key, String result) {
        kafkaTemplate.send(topic, key, result);
    }
}