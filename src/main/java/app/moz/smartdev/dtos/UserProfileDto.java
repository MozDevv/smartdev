package app.moz.smartdev.dtos;

import app.moz.smartdev.entity.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Data
public class UserProfileDto {

    private UUID id;
    private UUID userId;
    private String bio;
    private String skills;
    private String learningGoals;
    private String githubUsername;
    private String linkedinProfile;
    private String portfolioWebsite;
    private String profilePictureUrl;
    private String contactEmail;
    private String contactPhone;
    private String location;
    private List<String> certifications;
    private List<String> achievements;
    private Integer codingStreakDays;
    private String preferredLanguages;
    private String timezone;
    private String availability;
    private String experienceLevel;

 
}