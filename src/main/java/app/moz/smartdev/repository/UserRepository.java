package app.moz.smartdev.repository;

import app.moz.smartdev.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface UserRepository extends JpaRepository<User, UUID> {

    @EntityGraph(attributePaths = {"roles"})
    List<User> findAll();

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    Optional<User> findByActivationCode(String activationCode);
}
