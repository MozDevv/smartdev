package app.moz.smartdev.service;

import app.moz.smartdev.dtos.OauthTokenDto;
import app.moz.smartdev.entity.OauthToken;
import app.moz.smartdev.entity.User;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.UUID;

public interface OauthTokenService {

    List<OauthTokenDto> findAll();

    List<OauthTokenDto> findById(UUID id);

    OauthTokenDto create(OauthTokenDto oauthToken);

    OauthToken saveOAuthToken(String accessToken, String providerName, HttpServletRequest request);
}
