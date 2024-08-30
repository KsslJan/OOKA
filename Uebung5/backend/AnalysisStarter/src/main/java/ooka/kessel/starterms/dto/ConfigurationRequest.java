package ooka.kessel.starterms.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ConfigurationRequest {
    private String cylinder;
    private String gearbox;
    // to send to analysis service for referall when producing message over Kafka
    private String optionKey;

}
