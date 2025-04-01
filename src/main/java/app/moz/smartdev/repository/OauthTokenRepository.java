package app.moz.smartdev.repository;

import app.moz.smartdev.entity.OauthToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OauthTokenRepository extends JpaRepository<OauthToken, UUID> {

    List<OauthToken> findByUserId(UUID userId);
}
