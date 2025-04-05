package app.moz.smartdev.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Owner {
    @JsonProperty("login")
    private String login;
}
