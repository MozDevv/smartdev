package app.moz.smartdev.service;

import app.moz.smartdev.dtos.OauthTokenDto;

import java.util.List;
import java.util.UUID;

public interface OauthTokenService {

    List<OauthTokenDto> findAll();

    List<OauthTokenDto> findById(UUID id);

    OauthTokenDto create(OauthTokenDto oauthToken);
}
