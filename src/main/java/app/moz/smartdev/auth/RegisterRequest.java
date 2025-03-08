package app.moz.smartdev.auth;

import app.moz.smartdev.dtos.RolesDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {


    private String email;

    private String username;

    private String password;

    private  String profilePicture;

    private String status;

    private UUID roleId;


}
