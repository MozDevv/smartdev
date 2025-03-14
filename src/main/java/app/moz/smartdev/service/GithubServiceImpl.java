package app.moz.smartdev.service;

import app.moz.smartdev.entity.OauthToken;
import app.moz.smartdev.entity.User;
import app.moz.smartdev.repository.OauthTokenRepository;
import app.moz.smartdev.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@Builder
@AllArgsConstructor
@Data
public class GithubServiceImpl implements GithubService {

    private final OauthTokenRepository oauthTokenRepository;
    private final UserRepository userRepository;


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
}