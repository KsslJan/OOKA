package ooka.kessel.starterms.api;

import ooka.kessel.starterms.dto.AnalysisRequest;
import ooka.kessel.starterms.dto.AnalysisResult;
import ooka.kessel.starterms.dto.ConfigurationRequest;
import ooka.kessel.starterms.dto.WebsocketResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

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
        logger.debug("Initializing service port mapping");
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
        logger.debug("Received analysis request: {}", analysisRequest);
        ConfigurationRequest configRequest = new ConfigurationRequest("V12", "2026");
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
                WebClient webClient = WebClient.builder().baseUrl(baseUrl + port).build();
                webClient.post().uri(endpoint)
                        .accept(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromObject(configRequest))
                        .retrieve()
                        .bodyToMono(AnalysisResult.class)
                        .doOnSuccess(result -> {
                            messagingTemplate.convertAndSend("/results/analysisResult", new WebsocketResult(key, result.isAnalysisSuccessful()) {
                            });
                            System.out.println("Sending result: " + key + ", " + result.isAnalysisSuccessful());
                        })
                        .doOnError(throwable -> {
                            messagingTemplate.convertAndSend("/results/analysisResult", new WebsocketResult(key, false));
                            throwable.printStackTrace();
                        })
                        .subscribe();
            }
        });
        return new ResponseEntity<>(results, HttpStatus.ACCEPTED);
    }

}
