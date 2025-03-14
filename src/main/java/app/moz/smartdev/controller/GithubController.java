package app.moz.smartdev.controller;

import app.moz.smartdev.service.GithubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/github")
public class GithubController {

    private final GithubService githubService;

    @Autowired
    public GithubController(GithubService githubService) {
        this.githubService = githubService;
    }

    @GetMapping("/callback")
    public String saveOAuthToken(@RegisteredOAuth2AuthorizedClient("github") OAuth2AuthorizedClient authorizedClient, OAuth2User oAuth2User) {
        githubService.saveOAuthToken(authorizedClient, oAuth2User);
        return "Token saved successfully";
    }
}