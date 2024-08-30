package ooka.kessel.engineanalysis.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import ooka.kessel.engineanalysis.dto.AnalysisResult;
import ooka.kessel.engineanalysis.dto.ConfigurationRequest;
import ooka.kessel.engineanalysis.kafka.AnalysisResultProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Random;

// @RestController
@Service
public class EngineAnalysisController {

    private final AnalysisResultProducer resultProducer;
    private final ObjectMapper objectMapper;

    private final static Logger logger = LoggerFactory.getLogger(EngineAnalysisController.class);

    @Autowired
    public EngineAnalysisController(AnalysisResultProducer resultProducer, ObjectMapper objectMapper) {
        this.resultProducer = resultProducer;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "engine-analysis", groupId = "engine-analysis-1", containerFactory = "kafkaListenerContainerFactory")
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

    // @PostMapping("/analyse")
    // public ResponseEntity<Map<String, Boolean>> analyseConfiguration(@RequestBody ConfigurationRequest configurationRequest) {
    //     boolean analysisSuccessful = new Random().nextDouble() < 0.6;
    //     int timeout = new Random().nextInt((6000 - 1000) + 1) + 1000;
    //     try {
    //         Thread.sleep(timeout);
    //     } catch (InterruptedException e) {
    //         Thread.currentThread().interrupt();
    //     }
    //     AnalysisResult result = new AnalysisResult(analysisSuccessful, configurationRequest.getOptionKey());
    //     String resultString;
    //     try {
    //         resultString = objectMapper.writeValueAsString(result);
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    //     }
    //     resultProducer.sendAnalysisResult("analysis-results", configurationRequest.getOptionKey(), resultString);
    //     return new ResponseEntity<>(HttpStatus.ACCEPTED);
    // }
}
