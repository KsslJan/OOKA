package ooka.kessel.startingsystemanalysis.api;

import ooka.kessel.startingsystemanalysis.dto.ConfigurationRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
public class StartingSystemAnalysisController {

    @PostMapping("/analyse")
    public ResponseEntity<Map<String, Boolean>> analyseConfiguration(@RequestBody ConfigurationRequest configurationRequest) {
        int timeout = new Random().nextInt((6000 - 1000) + 1) + 1000;
        boolean analysisSuccessful = new Random().nextDouble() < 0.6;
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        Map<String, Boolean> body = new HashMap<>();
        body.put("analysisSuccessful", analysisSuccessful);
        return new ResponseEntity<>(body, HttpStatus.OK);
    }
}
