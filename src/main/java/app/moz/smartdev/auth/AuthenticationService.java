package app.moz.smartdev.auth;

import app.moz.smartdev.configs.JwtService;
import app.moz.smartdev.entity.User;
import app.moz.smartdev.repository.UserRepository;
import app.moz.smartdev.service.EmailService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
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
    private final EmailService emailService;

    private String generateRandomPassword() {
        return RandomStringUtils.random(12, true, true) + "!@#";
    }

    public AuthenticationResponse register(RegisterRequest request) {
        Optional<User> userDto = userRepository.findByEmail(request.getEmail());

        if (userDto.isPresent()) {
            throw new IllegalArgumentException("Email Already Exists");
        }

        String password = generateRandomPassword();

        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .profilePicture(request.getProfilePicture())
                .password(passwordEncoder.encode(password))
                .activationCode(UUID.randomUUID().toString())
                .status("PENDING")
                .build();
        var user1 = userRepository.save(user);

        var jwtToken = jwtService.generateToken((UserDetails) user1);

        sendActivationEmail(user1, password);

        return AuthenticationResponse.builder()
                .profilePicture(user1.getProfilePicture())
                .username(user1.getUsername())
                .email(user1.getEmail())
                .password(user1.getPassword())
                .message("Check your email to activate your account")
                .status(user1.getStatus())
                .activationCode(user1.getActivationCode())
                .id(user1.getId())
                .token(jwtToken)
                .build();
    }

    public void sendActivationEmail(User user, String password) {
        String activationLink = "http://localhost:8080/api/v1/auth/activate?code=" + user.getActivationCode();

        String message = String.format(
                "<p>Dear %s,</p>"
                        + "<p>Thank you for registering with us! Below are your account details:</p>"
                        + "<p>=====================================<br>"
                        + "Username: %s<br>"
                        + "Temporary Password: %s<br>"
                        + "=====================================</p>"
                        + "<p>Please click the link below to activate your account and set a new password:<br>"
                        + "<a href=\"%s\">%s</a></p>"
                        + "<p>For your security, please change your password after logging in.</p>"
                        + "<p>If you did not create this account, please ignore this email.</p>"
                        + "<p>Best regards,<br>Your Company Name</p>",
                user.getUsername(),
                user.getEmail(),
                password,
                activationLink,
                activationLink
        );

        emailService.sendEmail(user.getEmail(), "Activate Your Account", message);
    }


    public AuthenticationResponse changePassword(ChangePasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
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
                .message("Password changed successfully")
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

        if (user.getStatus().equals("PENDING")) {
            throw new IllegalArgumentException("Account not activated");
        }
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