package app.moz.smartdev.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class AuthProvider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String providerName;           // e.g., "GitHub", "Google", "Slack"
    private String clientId;               // OAuth Client ID
    private String clientSecret;           // OAuth Client Secret
    private String authorizationUri;       // URL to initiate OAuth authorization
    private String tokenUri;               // URL to exchange the authorization code for a token
    private String redirectUri;            // URL where the provider will redirect after authorization
    private String scope;                  // Scopes requested during the OAuth process
    private String userInfoUri;            // (Optional) URL to fetch user profile information
    private String grantType;              // e.g., "authorization_code", "client_credentials"
    private boolean isActive;              // Flag to enable/disable provider

    // Constructor without id
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