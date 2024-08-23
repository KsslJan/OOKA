package ooka.kessel.starterms.dto;


import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class AnalysisRequest {

    private boolean startingSystem;
    private boolean auxPTO;
    private boolean oilSystem;
    private boolean fuelSystem;
    private boolean coolingSystem;
    private boolean exhaustSystem;
    private boolean mountingSystem;
    private boolean engineManagementSystem;
    private boolean monitoringControlSystem;
    private boolean powerTransmission;
    private boolean gearBoxOptions;
}
