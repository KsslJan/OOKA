package ooka.kessel.engineanalysis.api;

import ooka.kessel.engineanalysis.dto.ConfigurationRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class EngineAnalysisController {

    @PostMapping("/analyse")
    public ResponseEntity<String> analyseConfiguration(@RequestBody ConfigurationRequest configurationRequest) {
        // simulate
        int timeout = new Random().nextInt((6000 - 1000) + 1) + 1000;
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return new ResponseEntity<>("Analysis on exhaus system started.", HttpStatus.OK);
    }
}
