package ooka.kessel.fluidanalysisms.api;

import ooka.kessel.fluidanalysisms.dto.ConfigurationRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

@RestController
public class FluidAnalysisController {
    private static final Logger LOG = Logger.getLogger(FluidAnalysisController.class.getName());

    @PostMapping("/analyze")
    public ResponseEntity<Map<String, Boolean>> analyseConfiguration(@RequestBody ConfigurationRequest configurationRequest) {
        LOG.info("Received request: " + configurationRequest);

        // simulate
        boolean analysisSuccesful = new Random().nextDouble() < 0.6;
        int timeout = new Random().nextInt((6000 - 1000) + 1) + 1000;
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        Map<String, Boolean> body = new HashMap<>();
        body.put("analysisSuccessful", analysisSuccesful);
        return new ResponseEntity<>(body, HttpStatus.OK);
    }
}
