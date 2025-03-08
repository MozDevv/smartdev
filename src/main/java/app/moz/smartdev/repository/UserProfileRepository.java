package app.moz.smartdev.repository;

import app.moz.smartdev.entity.User;
import app.moz.smartdev.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {

    UserProfile findByUser(User user);
}
