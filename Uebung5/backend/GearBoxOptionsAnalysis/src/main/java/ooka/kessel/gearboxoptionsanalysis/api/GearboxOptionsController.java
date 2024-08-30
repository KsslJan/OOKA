package ooka.kessel.gearboxoptionsanalysis.api;

import ooka.kessel.gearboxoptionsanalysis.dto.AnalysisResult;
import ooka.kessel.gearboxoptionsanalysis.dto.ConfigurationRequest;
import ooka.kessel.gearboxoptionsanalysis.kafka.AnalysisResultProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class GearboxOptionsController {

    private final AnalysisResultProducer resultProducer;

    private final static Logger logger = LoggerFactory.getLogger(GearboxOptionsController.class);

    @Autowired
    public GearboxOptionsController(AnalysisResultProducer resultProducer) {
        this.resultProducer = resultProducer;
    }

    @KafkaListener(topics = "gearbox-analysis", groupId = "gearbox-options-1", containerFactory = "kafkaListenerContainerFactory")
    public void listenToConfigurationRequest(@Payload ConfigurationRequest request, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic, @Header(KafkaHeaders.RECEIVED_PARTITION) String partition, ConsumerRecordMetadata metadata) {
        boolean analysisSuccessful = new Random().nextDouble() < 0.6;
        int timeout = new Random().nextInt((6000 - 1000) + 1) + 1000;
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        AnalysisResult analysisResult = new AnalysisResult(analysisSuccessful, request.getOptionKey());
        logger.info("Record received from topic " + topic + " in partition " + partition + " with message " + analysisResult + " with offset " + metadata.offset());
        resultProducer.sendAnalysisResult("analysis-results", analysisResult);
    }

    @PostMapping("/analyze")
    public ResponseEntity<Void> analyseConfiguration() {
        return ResponseEntity.ok().build();
    }
}