package ooka.kessel.gearboxoptionsanalysis.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ConfigurationRequest {
    private String cylinder;
    private String gearbox;
    private String optionKey;

}
