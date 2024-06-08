package ooka.kessel.transmissionanalysis.api;

import ooka.kessel.transmissionanalysis.dto.ConfigurationRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class TransmissionAnalysisController {

    @PostMapping("/analyse")
    public ResponseEntity<String> analyseConfiguration(@RequestBody ConfigurationRequest configurationRequest) {
        int timeout = new Random().nextInt((6000 - 1000) + 1) + 1000;
        boolean successfulAnalysis = new Random().nextDouble() < 0.6;
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return new ResponseEntity<>("Analysis complete!", HttpStatus.OK);
    }
}
