package ooka.kessel.fluidanalysisms.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import ooka.kessel.fluidanalysisms.dto.AnalysisResult;
import ooka.kessel.fluidanalysisms.dto.ConfigurationRequest;
import ooka.kessel.fluidanalysisms.kafka.AnalysisResultProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
public class FluidAnalysisController {

    private final AnalysisResultProducer resultProducer;

    private final static Logger logger = LoggerFactory.getLogger(FluidAnalysisController.class);

    @Autowired
    public FluidAnalysisController(AnalysisResultProducer resultProducer) {
        this.resultProducer = resultProducer;
    }

    @KafkaListener(topics = "fluid-analysis", groupId = "fluid-analysis-1", containerFactory = "kafkaListenerContainerFactory")
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
