package app.moz.smartdev.repository;

import app.moz.smartdev.entity.AuthProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface AuthProviderRepository extends JpaRepository<AuthProvider, Long> {
}