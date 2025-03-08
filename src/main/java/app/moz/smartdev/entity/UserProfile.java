package app.moz.smartdev.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "user_profiles")
public class UserProfile {

    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(name = "skills", columnDefinition = "TEXT")
    private String skills;

    private String learningGoals;
}
