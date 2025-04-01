package app.moz.smartdev.service;

import app.moz.smartdev.dtos.ProviderDto;

import java.util.List;

public interface ProviderService {
    ProviderDto create(ProviderDto providerDto);

    List<ProviderDto> findAll();
}
