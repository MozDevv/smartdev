package app.moz.smartdev.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthProviderDto {
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
}
