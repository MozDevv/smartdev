package app.moz.smartdev.service;

import app.moz.smartdev.dtos.AuthProviderDto;
import app.moz.smartdev.entity.AuthProvider;
import app.moz.smartdev.repository.AuthProviderRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthProviderServiceImpl implements AuthProviderService {

    private final AuthProviderRepository authProviderRepository;
    private final ModelMapper modelMapper;


    @Override
    public AuthProviderDto create(AuthProviderDto authProviderDto) {

        AuthProvider authProvider = new AuthProvider();
        authProvider.setProviderName(authProviderDto.getProviderName());

        try{
            AuthProvider authProvider2 = authProviderRepository.save(authProvider);
            return modelMapper.map(authProvider2, AuthProviderDto.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<AuthProviderDto> getProviders() {

        return authProviderRepository.findAll().stream()
                .map((authProvider -> modelMapper.map(authProvider, AuthProviderDto.class)))
                .toList();

    }

    @Override
    public List<AuthProviderDto> getProvidersByUserId(UUID userId) {
        return List.of();
    }
}
