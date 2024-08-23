package ooka.kessel.starterms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class ConfigurationRequest {
    private String cylinder;
    private String gearbox;
    // to send to analysis service for referall when producing message over Kafka
    private String optionKey;

}
