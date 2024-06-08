package ooka.kessel.starterms.api;

import ooka.kessel.starterms.dto.AnalysisRequest;
import ooka.kessel.starterms.dto.ConfigurationRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@RestController
public class StarterController {

    private final String baseUrl = "http://localhost:";
    private final String endpoint = "/analyse";
    private final Map<String, Boolean> results = new HashMap<>();


    @PostMapping("/analyse")
    public ResponseEntity<Map<String, Boolean>> startAnalysis(@RequestBody AnalysisRequest analysisRequest) {
        WebClient webClient = null;

        if (analysisRequest.isAuxPTO()) {
            webClient = WebClient.builder().baseUrl(baseUrl).build();
            webClient.post().uri(endpoint).body(BodyInserters.fromObject(new ConfigurationRequest("V12", "2026"))).retrieve().bodyToMono(Boolean.class).subscribe(
                    analysisSuccessful -> {
                        results.put("auxPTO", analysisSuccessful);
                    }, throwable -> {
                        System.out.println(throwable);
                    }
            );

        }

        if (analysisRequest.isCoolingSystem()) {
            webClient = WebClient.builder().baseUrl(baseUrl).build();
            webClient.post().uri(endpoint).body(BodyInserters.fromObject(new ConfigurationRequest("V12", "2026"))).retrieve().bodyToMono(Boolean.class).subscribe(
                    analysisSuccessful -> {
                        results.put("auxPTO", analysisSuccessful);
                    }, throwable -> {
                        System.out.println(throwable);
                    }
            );
        }

        if (analysisRequest.isFuelSystem()) {
            webClient = WebClient.builder().baseUrl(baseUrl).build();
            webClient.post().uri(endpoint).body(BodyInserters.fromObject(new ConfigurationRequest("V12", "2026"))).retrieve().bodyToMono(Boolean.class).subscribe(
                    analysisSuccessful -> {
                        results.put("auxPTO", analysisSuccessful);
                    }, throwable -> {
                        System.out.println(throwable);
                    }
            );
        }

        if (analysisRequest.isEngineManagementSystem()) {
            webClient = WebClient.builder().baseUrl(baseUrl).build();
            webClient.post().uri(endpoint).body(BodyInserters.fromObject(new ConfigurationRequest("V12", "2026"))).retrieve().bodyToMono(Boolean.class).subscribe(
                    analysisSuccessful -> {
                        results.put("auxPTO", analysisSuccessful);
                    }, throwable -> {
                        System.out.println(throwable);
                    }
            );
        }

        if (analysisRequest.isMonitoringControlSystem()) {
            webClient = WebClient.builder().baseUrl(baseUrl).build();
            webClient.post().uri(endpoint).body(BodyInserters.fromObject(new ConfigurationRequest("V12", "2026"))).retrieve().bodyToMono(Boolean.class).subscribe(
                    analysisSuccessful -> {
                        results.put("auxPTO", analysisSuccessful);
                    }, throwable -> {
                        System.out.println(throwable);
                    }
            );
        }

        if (analysisRequest.isStartingSystem()) {
            webClient = WebClient.builder().baseUrl(baseUrl).build();
            webClient.post().uri(endpoint).body(BodyInserters.fromObject(new ConfigurationRequest("V12", "2026"))).retrieve().bodyToMono(Boolean.class).subscribe(
                    analysisSuccessful -> {
                        results.put("auxPTO", analysisSuccessful);
                    }, throwable -> {
                        System.out.println(throwable);
                    }
            );
        }

        if (analysisRequest.isExhaustSystem()) {
            webClient = WebClient.builder().baseUrl(baseUrl).build();
            webClient.post().uri(endpoint).body(BodyInserters.fromObject(new ConfigurationRequest("V12", "2026"))).retrieve().bodyToMono(Boolean.class).subscribe(
                    analysisSuccessful -> {
                        results.put("auxPTO", analysisSuccessful);
                    }, throwable -> {
                        System.out.println(throwable);
                    }
            );
        }

        if (analysisRequest.isGearBoxOptions()) {
            webClient = WebClient.builder().baseUrl(baseUrl).build();
            webClient.post().uri(endpoint).body(BodyInserters.fromObject(new ConfigurationRequest("V12", "2026"))).retrieve().bodyToMono(Boolean.class).subscribe(
                    analysisSuccessful -> {
                        results.put("auxPTO", analysisSuccessful);
                    }, throwable -> {
                        System.out.println(throwable);
                    }
            );

        }

        if (analysisRequest.isOilSystem()) {
            webClient = WebClient.builder().baseUrl(baseUrl).build();
            webClient.post().uri(endpoint).body(BodyInserters.fromObject(new ConfigurationRequest("V12", "2026"))).retrieve().bodyToMono(Boolean.class).subscribe(
                    analysisSuccessful -> {
                        results.put("auxPTO", analysisSuccessful);
                    }, throwable -> {
                        System.out.println(throwable);
                    }
            );
        }

        if (analysisRequest.isMountingSystem()) {
            webClient = WebClient.builder().baseUrl(baseUrl).build();
            webClient.post().uri(endpoint).body(BodyInserters.fromObject(new ConfigurationRequest("V12", "2026"))).retrieve().bodyToMono(Boolean.class).subscribe(
                    analysisSuccessful -> {
                        results.put("auxPTO", analysisSuccessful);
                    }, throwable -> {
                        System.out.println(throwable);
                    }
            );
        }

        if (analysisRequest.isStartingSystem()) {
            webClient = WebClient.builder().baseUrl(baseUrl).build();
            webClient.post().uri(endpoint).body(BodyInserters.fromObject(new ConfigurationRequest("V12", "2026"))).retrieve().bodyToMono(Boolean.class).subscribe(
                    analysisSuccessful -> {
                        results.put("auxPTO", analysisSuccessful);
                    }, throwable -> {
                        System.out.println(throwable);
                    }
            );
        }
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

}
