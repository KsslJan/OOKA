package ooka.kessel.engineanalysis.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import ooka.kessel.engineanalysis.dto.AnalysisResult;
import ooka.kessel.engineanalysis.dto.ConfigurationRequest;
import ooka.kessel.engineanalysis.kafka.AnalysisResultProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
public class EngineAnalysisController {

    private final AnalysisResultProducer resultProducer;
    private final ObjectMapper objectMapper;

    @Autowired
    public EngineAnalysisController(AnalysisResultProducer resultProducer, ObjectMapper objectMapper) {
        this.resultProducer = resultProducer;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/analyse")
    public ResponseEntity<Map<String, Boolean>> analyseConfiguration(@RequestBody ConfigurationRequest configurationRequest) {
        // simulate

        boolean analysisSuccessful = new Random().nextDouble() < 0.6;
        // boolean analysisSuccessful = true;
        int timeout = new Random().nextInt((6000 - 1000) + 1) + 1000;
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        AnalysisResult result = new AnalysisResult(analysisSuccessful, configurationRequest.getOptionKey());
        String resultString;
        try {
            resultString = objectMapper.writeValueAsString(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        resultProducer.sendAnalysisResult("analysis-results", configurationRequest.getOptionKey(), resultString);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
