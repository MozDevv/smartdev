package app.moz.smartdev.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "user_roles")
public class UserRole {

    @EmbeddedId
    private UserRoleId id;  // ✅ Correct usage of @EmbeddedId
}
