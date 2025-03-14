package app.moz.smartdev.auth;


import app.moz.smartdev.dtos.RolesDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {


    private UUID id;

    private String email;

    private String username;

    private String password;

    private String profilePicture;

    private String status;

    private UUID roleId;


    private List<RolesDto> roles;

    private String token;

    private String refresh_token;

    private String message;

    private String activationCode;


}
