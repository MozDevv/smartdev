package app.moz.smartdev.auth;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<?> register (
            @RequestBody RegisterRequest request
    ) {

    try{
        return ResponseEntity.ok(authenticationService.register(request));
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login (
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authenticationService.login(request));

    }

    @PostMapping("/change-password/{userId}")
    public  ResponseEntity<AuthenticationResponse> changePassword (
            @PathVariable UUID userId, @RequestBody ChangePasswordRequest request
    ) {
        return ResponseEntity.ok(authenticationService.changePassword(request, userId));
    }

}
