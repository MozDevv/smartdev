package app.moz.smartdev.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class AuthProvider {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String providerName;           // e.g., "GitHub", "Google", "Slack"
    private String clientId;               // OAuth Client ID
    private String clientSecret;           // OAuth Client Secret
    private String authorizationUri;       // URL to initiate OAuth authorization
    private String tokenUri;               // URL to exchange the authorization code for a token
    private String redirectUri;            // URL where the provider will redirect after authorization
    private String scope;                  // Scopes requested during the OAuth process
    private String userInfoUri;            // (Optional) URL to fetch user profile information
    private String grantType;              // e.g., "authorization_code", "client_credentials"
    private boolean isActive;

    private UUID userId;   // Flag to enable/disable provider

    // Add this constructor
    public AuthProvider(String providerName, String clientId, String clientSecret, String authorizationUri, String tokenUri, String redirectUri, String scope, String userInfoUri, String grantType, boolean isActive) {
        this.providerName = providerName;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.authorizationUri = authorizationUri;
        this.tokenUri = tokenUri;
        this.redirectUri = redirectUri;
        this.scope = scope;
        this.userInfoUri = userInfoUri;
        this.grantType = grantType;
        this.isActive = isActive;
    }

}