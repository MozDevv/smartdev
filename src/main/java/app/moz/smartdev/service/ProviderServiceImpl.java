package app.moz.smartdev.service;

import app.moz.smartdev.dtos.ProviderDto;
import app.moz.smartdev.entity.Provider;
import app.moz.smartdev.repository.ProviderRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Data
public class ProviderServiceImpl implements ProviderService {

    private final ProviderRepository providerRepository;
    private final ModelMapper modelMapper;

    @Override
    public ProviderDto create(ProviderDto providerDto) {

        Provider provider = new Provider();
        provider.setName(providerDto.getName());
//        provider.se

        try {
          Provider provider1 =  providerRepository.save(provider);
          return modelMapper.map(provider1, ProviderDto.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ProviderDto> findAll() {
      try{
        return providerRepository.findAll().stream()
                .map((provider -> modelMapper.map(provider, ProviderDto.class)))
                .toList();
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
}
