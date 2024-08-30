package ooka.kessel.starterms.kafka;

import ooka.kessel.starterms.dto.ConfigurationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class AnalysisRequestProducer {
    private final KafkaTemplate<String, ConfigurationRequest> kafkaTemplate;

    @Autowired
    public AnalysisRequestProducer(KafkaTemplate<String, ConfigurationRequest> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendAnalysisRequest(String topic, ConfigurationRequest request) {
        kafkaTemplate.send(topic, request);
    }
}
