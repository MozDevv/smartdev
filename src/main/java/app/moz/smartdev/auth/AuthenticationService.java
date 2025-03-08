package app.moz.smartdev.auth;

import app.moz.smartdev.configs.JwtService;
import app.moz.smartdev.entity.User;
import app.moz.smartdev.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        Optional<User> userDto = userRepository.findByEmail(request.getEmail());

        if (userDto.isPresent()) {
            throw new IllegalArgumentException("Email Already Exists");
        }

        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .profilePicture(request.getProfilePicture())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        var user1 = userRepository.save(user);
        var jwtToken = jwtService.generateToken((UserDetails) user1);

        return AuthenticationResponse.builder()
                .profilePicture(user1.getProfilePicture())
                .username(user1.getUsername())
                .email(user1.getEmail())
                .password(user1.getPassword())
                .id(user1.getId())
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse changePassword(ChangePasswordRequest request, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new BadCredentialsException("Password is Incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        User updatedUser = userRepository.save(user);
        String jwtToken = jwtService.generateToken((UserDetails) updatedUser);

        return AuthenticationResponse.builder()
                .username(updatedUser.getUsername())
                .profilePicture(updatedUser.getProfilePicture())
                .email(updatedUser.getEmail())
                .password(updatedUser.getPassword())
                .id(updatedUser.getId())
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse login(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();


        var jwtToken = jwtService.generateToken(user);

        List<String> permissionsList2 = Arrays.asList("read", "write", "view");

        return AuthenticationResponse.builder()
                .refresh_token(user.getRefresh_token())
                .id(user.getId())
                .token(jwtToken)
                .email(user.getEmail())
                .build();
    }
}