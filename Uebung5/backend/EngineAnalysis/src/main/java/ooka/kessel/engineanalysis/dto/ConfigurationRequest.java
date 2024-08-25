package ooka.kessel.engineanalysis.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ConfigurationRequest {
    private String cylinder;
    private String gearbox;

}
