package app.moz.smartdev.repository;

import app.moz.smartdev.entity.AuthProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

public interface AuthProviderRepository extends JpaRepository<AuthProvider, Long> {
    AuthProvider findByUserId(UUID userId);
    Optional<AuthProvider> findById(UUID id);
    AuthProvider findByProviderName(String providerName);
    Optional<AuthProvider> findByUserIdAndProviderName(UUID userId, String providerName);
}