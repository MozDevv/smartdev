package app.moz.smartdev.service;

import app.moz.smartdev.entity.AuthProvider;
import app.moz.smartdev.entity.OauthToken;
import app.moz.smartdev.entity.User;
import app.moz.smartdev.repository.AuthProviderRepository;
import app.moz.smartdev.repository.OauthTokenRepository;
import app.moz.smartdev.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@Builder
@AllArgsConstructor
@Data
@Slf4j
public class GithubServiceImpl implements GithubService {

    private final OauthTokenRepository oauthTokenRepository;
    private final UserRepository userRepository;
    private final AuthProviderRepository authProviderRepository;
    private final ModelMapper modelMapper;


    public void saveOAuthToken(@RegisteredOAuth2AuthorizedClient("github") OAuth2AuthorizedClient authorizedClient, OAuth2User oAuth2User) {
        String accessToken = authorizedClient.getAccessToken().getTokenValue();
        String refreshToken = authorizedClient.getRefreshToken() != null ? authorizedClient.getRefreshToken().getTokenValue() : null;

        User user = userRepository.findByUsername(oAuth2User.getName()).orElse(new User());
        user.setUsername(oAuth2User.getName());
        user.setEmail(oAuth2User.getAttribute("email"));
        userRepository.save(user);

        OauthToken oauthToken = new OauthToken();
        oauthToken.setUser(user);
        oauthToken.setAccessToken(accessToken);
        oauthToken.setRefreshToken(refreshToken);
        oauthTokenRepository.save(oauthToken);
    }

    public OauthToken saveOAuthToken(String accessToken, User user) {
        Logger logger = LoggerFactory.getLogger(GithubServiceImpl.class);
        logger.info("Starting saveOAuthToken method");

        AuthProvider authProvider = authProviderRepository.findByProviderName("GitHub");
        if (authProvider == null) {
            logger.error("AuthProvider not found for 'GitHub'");
            throw new IllegalArgumentException("AuthProvider not found for 'GitHub'");
        }
        logger.info("AuthProvider found: {}", authProvider);

        OauthToken oauthToken = oauthTokenRepository.findOauthTokenByUserAndProvider(user, authProvider);
        if (oauthToken == null) {
            oauthToken = new OauthToken();
            oauthToken.setUser(user);
            oauthToken.setProvider(authProvider);
            logger.info("OauthToken created: {}", oauthToken);
        } else {
            logger.info("OauthToken found: {}", oauthToken);
        }

        oauthToken.setAccessToken(accessToken);
        oauthTokenRepository.save(oauthToken);
        logger.info("OauthToken saved: {}", oauthToken);

        OauthToken mappedToken = modelMapper.map(oauthToken, OauthToken.class);
        logger.info("OauthToken mapped: {}", mappedToken);

        return mappedToken;
    }
}