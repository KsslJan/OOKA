package ooka.kessel.starterms.api;

import ooka.kessel.starterms.dto.AnalysisRequest;
import ooka.kessel.starterms.dto.ConfigurationRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
public class StarterController {

    private final String baseUrl = "http://localhost:";
    private final String endpoint = "/analyse";
    private final Map<String, Boolean> results = new HashMap<>();


    @PostMapping("/analyse")
    public ResponseEntity<String> startAnalysis(@RequestBody AnalysisRequest analysisRequest) {
        WebClient webClient = null;

        if (analysisRequest.isAuxPTO()) {
            HttpEntity<ConfigurationRequest> request = new HttpEntity<>(new ConfigurationRequest("V12", "2026"));
            WebClient.builder().baseUrl(baseUrl).build();

            Mono<Boolean> response = (Mono<Boolean>) webClient.post().uri(endpoint).body(BodyInserters.fromObject(new ConfigurationRequest("V12", "2026"))).retrieve().bodyToMono(Boolean.class).subscribe(
                    analysisSuccessful -> {
                        results.put("auxPTO", analysisSuccessful);
                    }, throwable -> {
                        System.out.println(throwable);
                    }
            );

        }

        if (analysisRequest.isCoolingSystem()) {

        }

        if (analysisRequest.isFuelSystem()) {

        }

        if (analysisRequest.isEngineManagementSystem()) {

        }

        if (analysisRequest.isMonitoringControlSystem()) {

        }

        if (analysisRequest.isStartingSystem()) {

        }

        if (analysisRequest.isExhaustSystem()) {

        }

        if (analysisRequest.isGearBoxOptions()) {

        }

        if (analysisRequest.isOilSystem()) {

        }

        if (analysisRequest.isMountingSystem()) {

        }

        if (analysisRequest.isStartingSystem()) {

        }

        return new ResponseEntity<>("Analysis complete!", HttpStatus.OK);
    }

}
