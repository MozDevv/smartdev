package app.moz.smartdev.controller;

import app.moz.smartdev.configs.JwtService;
import app.moz.smartdev.entity.AuthProvider;
import app.moz.smartdev.entity.OauthToken;
import app.moz.smartdev.entity.User;
import app.moz.smartdev.repository.AuthProviderRepository;
import app.moz.smartdev.repository.OauthTokenRepository;
import app.moz.smartdev.repository.UserRepository;
import app.moz.smartdev.service.GitRepoService;
import app.moz.smartdev.service.GithubService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationExchange;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationResponse;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@AllArgsConstructor
public class GithubController {

    @Autowired
    private GithubService githubService;

    @Autowired
    private DefaultAuthorizationCodeTokenResponseClient tokenResponseClient;



    private final JwtService jwtService;
    private  final UserRepository userRepository;
    private final GitRepoService gitRepoService;
    private final OauthTokenRepository oauthTokenRepository;
    private final AuthProviderRepository authProviderRepository;


    private static final String CLIENT_ID = "Ov23ctG6bWv9zmt4gdVu";
    private static final String CLIENT_SECRET = "8ab6a54a99fe8f6862110e4c35f5c528bb532d0a";
    private static final String AUTHORIZATION_URI = "https://github.com/login/oauth/authorize";
    private static final String TOKEN_URI = "https://github.com/login/oauth/access_token";
    private static final String REDIRECT_URI = "http://localhost:8080/api/github/test";

    @GetMapping("/api/github/login")
    public ResponseEntity<Void> login() {
        String authorizationRequestUri = AUTHORIZATION_URI + "?client_id=" + CLIENT_ID + "&redirect_uri=" + REDIRECT_URI + "&scope=read:user,repo&response_type=code";
        log.info(authorizationRequestUri);
        return ResponseEntity.status(302).header("Location", authorizationRequestUri).build();
    }

    @GetMapping("/api/github/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Test successful");
    }

    @GetMapping("/api/github/user/repositories")
    public ResponseEntity<Map<String, Object>> getUserRepositories(HttpServletRequest request) {
        Map<String, Object> response = gitRepoService.getAllUserRepositories(request);
        if (response.containsKey("error")) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/github/repositories")
    public ResponseEntity<String> repositories(@RequestParam(value = "user_id", required = false) String userId) {
        if (userId != null) {
            AuthProvider authProvider = authProviderRepository.findByProviderName("GitHub");

            if (authProvider != null) {

                User user = userRepository.findById(UUID.fromString(userId)).orElseThrow(() -> new IllegalArgumentException("User not found"));
                OauthToken oauthToken = oauthTokenRepository.findOauthTokenByUserAndProvider(user, authProvider);
                gitRepoService.fetchUserGitHubDataAsync(oauthToken.getAccessToken(), user);
                return ResponseEntity.ok("Repositories fetched successfully");
            } else {
                return ResponseEntity.badRequest().body("Access token not found");
            }
        } else {
            return ResponseEntity.badRequest().body("Missing user ID");
        }
    }


    @GetMapping("/api/github/callback")
    public ResponseEntity<String> saveOAuthToken(@RequestParam("code") String code, @RequestParam(value = "state", required = false) String state, HttpServletRequest request) throws JsonProcessingException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // Remove "Bearer " prefix

            String userId = jwtService.extractUserId(token);

            if (userId == null) {
                return ResponseEntity.status(401).body("Unauthorized");
            }
            UUID userId2 = UUID.fromString(jwtService.extractUserId(token));
            User user = userRepository.findById(userId2).orElseThrow(() -> new IllegalArgumentException("User not found"));


            OAuth2AuthorizationRequest authorizationRequest = OAuth2AuthorizationRequest.authorizationCode()
                    .clientId(CLIENT_ID)
                    .authorizationUri(AUTHORIZATION_URI)
                    .redirectUri(REDIRECT_URI)
                    .state(state)
                    .build();

            OAuth2AuthorizationResponse authorizationResponse = OAuth2AuthorizationResponse.success(code)
                    .redirectUri(REDIRECT_URI)
                    .state(state)
                    .build();

            OAuth2AuthorizationExchange authorizationExchange = new OAuth2AuthorizationExchange(authorizationRequest, authorizationResponse);
            ClientRegistration clientRegistration = ClientRegistration.withRegistrationId("github")
                    .clientId(CLIENT_ID)
                    .clientSecret(CLIENT_SECRET)
                    .authorizationUri(AUTHORIZATION_URI)
                    .tokenUri(TOKEN_URI)
                    .redirectUri(REDIRECT_URI)
                    .authorizationGrantType(org.springframework.security.oauth2.core.AuthorizationGrantType.AUTHORIZATION_CODE)
                    .build();
            // Manually request the token
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("client_id", CLIENT_ID);
            body.add("client_secret", CLIENT_SECRET);
            body.add("code", code);
            body.add("redirect_uri", REDIRECT_URI);
            body.add("grant_type", "authorization_code");

            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(TOKEN_URI, HttpMethod.POST, requestEntity, String.class);
            log.info("Raw token response: {}", responseEntity.getBody());
            // Parse the response manually
            String[] pairs = responseEntity.getBody().split("&");
            String accessToken = null;
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if ("access_token".equals(keyValue[0])) {
                    accessToken = keyValue[1];
                    break;
                }
            }

            if (accessToken == null) {
                throw new IllegalArgumentException("Access token not found in the response");

            }
            headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            HttpEntity<String> entity = new HttpEntity<>("", headers);
            ResponseEntity<String> userResponse = restTemplate.exchange(
                    "https://api.github.com/user",
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode userJson = objectMapper.readTree(userResponse.getBody());
            String githubUsername = userJson.get("login").asText();

            log.info("Raw user response: {}", userResponse.getBody());

            log.info("Github access token💥❌❗❗❗: {}", accessToken);
           githubService.saveOAuthToken(accessToken, user);

       //     gitRepoService.fetchUserGitHubDataAsync(accessToken, user, githubUsername);
            return ResponseEntity.ok("Token and user details saved successfully");
        }
        else {
            return ResponseEntity.status(401).body("Unauthorized");
        }
    }
}