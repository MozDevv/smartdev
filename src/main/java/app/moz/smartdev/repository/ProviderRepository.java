package app.moz.smartdev.repository;

import app.moz.smartdev.entity.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProviderRepository extends JpaRepository<Provider, Long> {

    Provider findByProviderId(Long providerId);
}
