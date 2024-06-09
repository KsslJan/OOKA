package ooka.kessel.starterms.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class WebsocketResult {
    @JsonProperty("key")
    private String key;
    @JsonProperty("value")
    private boolean analysisSuccessful;
}
