package ooka.kessel.starterms.api;

import ooka.kessel.starterms.dto.AnalysisRequest;
import ooka.kessel.starterms.dto.AnalysisResult;
import ooka.kessel.starterms.dto.ConfigurationRequest;
import ooka.kessel.starterms.dto.WebsocketResult;
import ooka.kessel.starterms.kafka.AnalysisRequestProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class StarterController {

    private final SimpMessagingTemplate messagingTemplate;
    private final AnalysisRequestProducer requestProducer;
    private static final Logger logger = LoggerFactory.getLogger(StarterController.class);

    // Service to port mapping
    private final Map<String, String> topicMapping = new HashMap<>();

    @Autowired
    public StarterController(SimpMessagingTemplate messagingTemplate, AnalysisRequestProducer requestProducer) {
        this.messagingTemplate = messagingTemplate;
        this.requestProducer = requestProducer;
        initializeTopicMapping();
    }

    private void initializeTopicMapping() {
        topicMapping.put("auxPTO", "transmission-analysis");
        topicMapping.put("powerTransmission", "transmission-analysis");
        topicMapping.put("coolingSystem", "fluid-analysis");
        topicMapping.put("fuelSystem", "fluid-analysis");
        topicMapping.put("oilSystem", "fluid-analysis");
        topicMapping.put("engineManagementSystem", "engine-analysis");
        topicMapping.put("monitoringControlSystem", "engine-analysis");
        topicMapping.put("mountingSystem", "exhaust-mounting-analysis");
        topicMapping.put("exhaustSystem", "exhaust-mounting-analysis");
        topicMapping.put("gearBoxOptions", "gearbox-analysis");
        topicMapping.put("startingSystem", "starting-system-analysis");
    }

    @MessageMapping("/results")
    @PostMapping("/analyze")
    public ResponseEntity<Void> startAnalysis(@RequestBody AnalysisRequest analysisRequest) {
        ConfigurationRequest configRequest = new ConfigurationRequest();
        configRequest.setCylinder("V12");
        configRequest.setGearbox("2026");

        Map<String, Boolean> analysisProperties = new HashMap<>();
        analysisProperties.put("auxPTO", analysisRequest.isAuxPTO());
        analysisProperties.put("coolingSystem", analysisRequest.isCoolingSystem());
        analysisProperties.put("fuelSystem", analysisRequest.isFuelSystem());
        analysisProperties.put("engineManagementSystem", analysisRequest.isEngineManagementSystem());
        analysisProperties.put("monitoringControlSystem", analysisRequest.isMonitoringControlSystem());
        analysisProperties.put("startingSystem", analysisRequest.isStartingSystem());
        analysisProperties.put("exhaustSystem", analysisRequest.isExhaustSystem());
        analysisProperties.put("gearBoxOptions", analysisRequest.isGearBoxOptions());
        analysisProperties.put("oilSystem", analysisRequest.isOilSystem());
        analysisProperties.put("mountingSystem", analysisRequest.isMountingSystem());
        analysisProperties.put("powerTransmission", analysisRequest.isPowerTransmission());

        analysisProperties.forEach((key, value) -> {
            if (value) {
                configRequest.setOptionKey(key);
                // Todo: Check if service is up!
                requestProducer.sendAnalysisRequest(topicMapping.get(key), configRequest);
            }
        });
        return ResponseEntity.accepted().build();
    }

    @SendTo("/results/analysisResult")
    @KafkaListener(topics = "analysis-results", groupId = "analysis-starter", containerFactory = "kafkaListenerContainerFactory")
    public void listenToAnalysisResults(@Payload AnalysisResult result, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic, @Header(KafkaHeaders.RECEIVED_PARTITION) String partition, ConsumerRecordMetadata metadata) {
        logger.info("Record received from topic {} in partition {} with message {} with offset {}", topic, partition, result, metadata.offset());
        messagingTemplate.convertAndSend("/results/analysisResult", new WebsocketResult(result.getOptionKey(), result.isAnalysisSuccessful()));
    }
}
