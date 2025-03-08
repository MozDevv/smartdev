package app.moz.smartdev.dtos;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolesDto {

    private String name;

    private  String description;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;





}
