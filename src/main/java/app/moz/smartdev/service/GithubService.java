package app.moz.smartdev.service;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface GithubService {


    void saveOAuthToken(@RegisteredOAuth2AuthorizedClient("github") OAuth2AuthorizedClient authorizedClient, OAuth2User oAuth2User);

}
