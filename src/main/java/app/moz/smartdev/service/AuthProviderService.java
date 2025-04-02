package app.moz.smartdev.service;

import app.moz.smartdev.dtos.AuthProviderDto;

import java.util.List;
import java.util.UUID;

public interface AuthProviderService {

    AuthProviderDto create(AuthProviderDto authProviderDto);

    List<AuthProviderDto> getProviders();

    List<AuthProviderDto> getProvidersByUserId(UUID userId);
}
