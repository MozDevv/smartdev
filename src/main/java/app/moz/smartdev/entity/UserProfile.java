package app.moz.smartdev.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
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

    @Column(name = "learning_goals", columnDefinition = "TEXT")
    private String learningGoals;

    @Column(name = "github_username")
    private String githubUsername;

    @Column(name = "linkedin_profile")
    private String linkedinProfile;

    @Column(name = "portfolio_website")
    private String portfolioWebsite;

    @Column(name = "profile_picture_url")
    private String profilePictureUrl;

    @Column(name = "contact_email")
    private String contactEmail;

    @Column(name = "contact_phone")
    private String contactPhone;

    @Column(name = "location")
    private String location;

    @ElementCollection
    @CollectionTable(name = "certifications", joinColumns = @JoinColumn(name = "profile_id"))
    @Column(name = "certification")
    private List<String> certifications;

    @ElementCollection
    @CollectionTable(name = "achievements", joinColumns = @JoinColumn(name = "profile_id"))
    @Column(name = "achievement")
    private List<String> achievements;

    @Column(name = "coding_streak_days")
    private Integer codingStreakDays;

    @Column(name = "preferred_languages")
    private String preferredLanguages;

    @Column(name = "timezone")
    private String timezone;

    @Column(name = "availability")
    private String availability;  // e.g., "Full-time", "Part-time", "Freelance"

    @Column(name = "experience_level")
    private String experienceLevel;  // e.g., "Junior", "Mid", "Senior", etc.

}