package app.moz.smartdev.repository;

import app.moz.smartdev.entity.AuthProvider;
import app.moz.smartdev.entity.OauthToken;
import app.moz.smartdev.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OauthTokenRepository extends JpaRepository<OauthToken, UUID> {

    List<OauthToken> findByUserId(UUID userId);

    Optional<OauthToken> findByUserAndProvider(User user, AuthProvider provider);
    OauthToken findByUser(User user);

    OauthToken findOauthTokenByUserAndProvider(User user, AuthProvider provider);

}
