package ooka.kessel.fluidanalysisms.api;

import ooka.kessel.fluidanalysisms.dto.ConfigurationRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class FluidAnalysisController {
    @PostMapping("/analysis")
    public ResponseEntity<String> analyseConfiguration(@RequestBody ConfigurationRequest configurationRequest) {
        // simulate
        int timeout = new Random().nextInt((6000 - 1000) + 1) + 1000;
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return new ResponseEntity<>("Analysis complete!", HttpStatus.OK);
    }
}
