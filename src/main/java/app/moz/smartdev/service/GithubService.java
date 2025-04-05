package app.moz.smartdev.service;

import app.moz.smartdev.entity.OauthToken;
import app.moz.smartdev.entity.User;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface GithubService {


    OauthToken saveOAuthToken(
            String accessToken,
            User user

    );

}
