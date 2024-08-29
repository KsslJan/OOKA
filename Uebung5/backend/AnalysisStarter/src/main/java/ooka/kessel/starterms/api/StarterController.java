package ooka.kessel.starterms.api;

import ooka.kessel.starterms.dto.AnalysisRequest;
import ooka.kessel.starterms.dto.AnalysisResult;
import ooka.kessel.starterms.dto.ConfigurationRequest;
import ooka.kessel.starterms.dto.WebsocketResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
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
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class StarterController {

    private final SimpMessagingTemplate messagingTemplate;
    private final String baseUrl = "http://localhost:";
    private final String endpoint = "/analyse";
    private final Map<String, Boolean> results = new ConcurrentHashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(StarterController.class);

    // Service to port mapping
    private final Map<String, String> servicePortMapping = new HashMap<>();

    @Autowired
    public StarterController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
        initializeServicePortMapping();
    }


    private void initializeServicePortMapping() {
        servicePortMapping.put("auxPTO", "8087");
        servicePortMapping.put("coolingSystem", "8084");
        servicePortMapping.put("fuelSystem", "8084");
        servicePortMapping.put("engineManagementSystem", "8082");
        servicePortMapping.put("monitoringControlSystem", "8082");
        servicePortMapping.put("startingSystem", "8086");
        servicePortMapping.put("exhaustSystem", "8083");
        servicePortMapping.put("gearBoxOptions", "8085");
        servicePortMapping.put("oilSystem", "8084");
        servicePortMapping.put("mountingSystem", "8083");
        servicePortMapping.put("powerTransmission", "8087");
    }

    @MessageMapping("/results")
    @PostMapping("/analyse")
    @SendTo("/results/analysisResult")
    public ResponseEntity<Map<String, Boolean>> startAnalysis(@RequestBody AnalysisRequest analysisRequest) {
        ConfigurationRequest configRequest = new ConfigurationRequest();
        configRequest.setCylinder("V12");
        configRequest.setGearbox("2026");
        initializeServicePortMapping();

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
                String port = servicePortMapping.get(key);
                configRequest.setOptionKey(key);
                WebClient webClient = WebClient.builder().baseUrl(baseUrl + port).build();
                webClient.post().uri(endpoint)
                        .accept(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(configRequest))
                        .retrieve()
                        .onStatus(httpStatusCode -> !httpStatusCode.is2xxSuccessful(),
                                clientResponse -> Mono.error(new Exception("Failed to start analysis on service " + key)))
                        // either way no response body expected
                        .bodyToMono(Void.class)
                        .doOnError(throwable -> {
                            messagingTemplate.convertAndSend("/results/analysisResult", new WebsocketResult(key, false));
                            System.out.println(throwable.getMessage());
                            throwable.printStackTrace();
                        })
                        .subscribe();
            }
        });
        return new ResponseEntity<>(results, HttpStatus.ACCEPTED);
    }

    @SendTo("/results/analysisResult")
    @KafkaListener(topics = "analysis-results", groupId = "analysis-starter", topicPartitions = { @TopicPartition(topic = "analysis-results", partitions = "0") } )
    public void listenToAnalysisResults(@Payload AnalysisResult result, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic, @Header(KafkaHeaders.RECEIVED_PARTITION) String partition, ConsumerRecordMetadata metadata) {
        logger.info("Record received from topic " + topic + " in partition " + partition + " with message " + result + " with offset " + metadata.offset() );
        messagingTemplate.convertAndSend("/results/analysisResult", new WebsocketResult(result.getOptionKey(), result.isAnalysisSuccessful()));
    }
}
