package ooka.kessel.gearboxoptionsanalysis.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@Setter
public class AnalysisResult {
    private boolean analysisSuccessful;
    private String optionKey;
}
