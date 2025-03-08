package app.moz.smartdev.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Embeddable  // ✅ Correct usage for composite keys
@Getter
@Setter
public class UserRoleId implements Serializable {

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "role_id", nullable = false)
    private UUID roleId;
}
