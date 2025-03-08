package app.moz.smartdev.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
import java.util.UUID;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    private String email;

    private String username;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String password;

    private  String profilePicture;

    private String status;

    private UUID roleId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<RolesDto> roles;


}
