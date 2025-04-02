package app.moz.smartdev.service;

import app.moz.smartdev.dtos.OauthTokenDto;
import app.moz.smartdev.entity.AuthProvider;
import app.moz.smartdev.entity.OauthToken;
import app.moz.smartdev.entity.User;
import app.moz.smartdev.repository.AuthProviderRepository;
import app.moz.smartdev.repository.OauthTokenRepository;
import app.moz.smartdev.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
@Data
public class OauthTokenImpl implements OauthTokenService {

    private final OauthTokenRepository oauthTokenRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final AuthProviderRepository providerRepository;


    @Override
    public List<OauthTokenDto> findAll() {
        try {
            return oauthTokenRepository.findAll().stream()
                    .map(oauthToken -> modelMapper.map(oauthToken, OauthTokenDto.class))
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public  List<OauthTokenDto> findById(UUID id) {
        try {
            Optional<User> user = userRepository.findById(id);
            if (user.isEmpty()) {
                throw new EntityNotFoundException("User not found");
            }

            List<OauthToken> oauthTokens = oauthTokenRepository.findByUserId(user.get().getId());

            if (oauthTokens.isEmpty()) {
                throw new EntityNotFoundException("OauthToken not found");
            }
            return oauthTokens.stream()
                    .map(oauthToken -> modelMapper
                            .map(oauthToken, OauthTokenDto.class)).toList();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public OauthTokenDto create(OauthTokenDto oauthToken) {
        Optional<User> user = userRepository.findById(oauthToken.getUserId());

        if(user.isEmpty()){
            throw new EntityNotFoundException("User not found");
        }
        Optional<AuthProvider> provider = providerRepository.findById(oauthToken.getProviderId());
        if(provider.isEmpty()){
            throw new EntityNotFoundException("Provider not found");
        }
        OauthToken oauthToken1 = modelMapper.map(oauthToken, OauthToken.class);
        oauthToken1.setUser(user.get());
        oauthToken1.setAccessToken(oauthToken.getAccessToken());
        oauthToken1.setRefreshToken(oauthToken.getRefreshToken());
        oauthToken1.setProvider(provider.get());
        return modelMapper.map(oauthTokenRepository.save(oauthToken1), OauthTokenDto.class);
    }
}
