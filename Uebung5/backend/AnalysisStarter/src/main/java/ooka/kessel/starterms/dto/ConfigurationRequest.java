package ooka.kessel.starterms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class ConfigurationRequest {
    private String cylinder;
    private String gearbox;

}
