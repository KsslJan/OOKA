package ooka.kessel.starterms.api;

import ooka.kessel.starterms.dto.AnalysisRequest;
import ooka.kessel.starterms.dto.AnalysisResult;
import ooka.kessel.starterms.dto.ConfigurationRequest;
import ooka.kessel.starterms.dto.WebsocketResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${ENGINE_ANALYSIS_URL}")
    private String engineAnalysisUrl;

    @Value("${EXHAUST_MOUNTING_SYSTEM_ANALYSIS_URL}")
    private String exhaustMountingSystemAnalysisUrl;

    @Value("${FLUID_ANALYSIS_URL}")
    private String fluidAnalysisUrl;

    @Value("${GEAR_BOX_OPTIONS_ANALYSIS_URL}")
    private String gearBoxOptionsAnalysisUrl;

    @Value("${STARTING_SYSTEM_ANALYSIS_URL}")
    private String startingSystemAnalysisUrl;

    @Value("${TRANSMISSION_ANALYSIS_URL}")
    private String transmissionAnalysisUrl;

    private final SimpMessagingTemplate messagingTemplate;
    private final String endpoint = "/analyze";
    private final Map<String, Boolean> results = new ConcurrentHashMap<>();

    // Service to port mapping
    private final Map<String, String> servicePortMapping = new HashMap<>();

    @Autowired
    public StarterController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
        initializeServicePortMapping();
    }


    private void initializeServicePortMapping() {
        servicePortMapping.put("auxPTO", transmissionAnalysisUrl);
        servicePortMapping.put("coolingSystem", fluidAnalysisUrl);
        servicePortMapping.put("fuelSystem", fluidAnalysisUrl);
        servicePortMapping.put("engineManagementSystem", engineAnalysisUrl);
        servicePortMapping.put("monitoringControlSystem", engineAnalysisUrl);
        servicePortMapping.put("startingSystem", startingSystemAnalysisUrl);
        servicePortMapping.put("exhaustSystem", exhaustMountingSystemAnalysisUrl);
        servicePortMapping.put("gearBoxOptions", gearBoxOptionsAnalysisUrl);
        servicePortMapping.put("oilSystem", fluidAnalysisUrl);
        servicePortMapping.put("mountingSystem", exhaustMountingSystemAnalysisUrl);
        servicePortMapping.put("powerTransmission", transmissionAnalysisUrl);
    }

    @MessageMapping("/results")
    @PostMapping("/analyze")
    @SendTo("/results/analysisResult")
    public ResponseEntity<Map<String, Boolean>> startAnalysis(@RequestBody AnalysisRequest analysisRequest) {
        ConfigurationRequest configRequest = new ConfigurationRequest("V12", "2026");
        initializeServicePortMapping();

        Map<String, Boolean> analysisProperties = mapAnalysisRequestToServiceKeys(analysisRequest);

        analysisProperties.forEach((key, value) -> {
            if (value) {
                String basedUrl = servicePortMapping.get(key);
                WebClient webClient = WebClient.builder().baseUrl(basedUrl).build();
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

    private static Map<String, Boolean> mapAnalysisRequestToServiceKeys(AnalysisRequest analysisRequest) {
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
        return analysisProperties;
    }

}
