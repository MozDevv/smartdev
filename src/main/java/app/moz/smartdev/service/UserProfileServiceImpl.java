package app.moz.smartdev.service;

import app.moz.smartdev.dtos.UserProfileDto;
import app.moz.smartdev.entity.User;
import app.moz.smartdev.entity.UserProfile;
import app.moz.smartdev.repository.UserProfileRepository;
import app.moz.smartdev.repository.UserRepository;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Data
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository userProfileRepository;

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<UserProfileDto> getAllUserProfiles() {

        try {
            List<UserProfileDto> userProfileDtos = userProfileRepository.findAll().stream().map(userProfile -> modelMapper.map(userProfile, UserProfileDto.class)).toList();
            return userProfileDtos;

        } catch (Exception exception) {
            throw new IllegalArgumentException(exception);
        }
    }

    @Override
    public UserProfileDto getUserProfileByUserId(UUID userId) {

        try {

            Optional<User> user = userRepository.findById(userId);

            if (user.isEmpty()) {
                throw new IllegalArgumentException("User not found");
            }

            User user1 = user.get();
            Optional<UserProfile> userProfileDto = Optional.ofNullable(userProfileRepository.findByUser(user1));

            if (userProfileDto.isEmpty()) {
                throw new IllegalArgumentException("User profile not found");
            }
            return modelMapper.map(userProfileDto.get(), UserProfileDto.class);
        } catch (Exception exception) {
            throw new IllegalArgumentException(exception);
        }
    }

    @Override
    public UserProfileDto createUserProfile(UserProfileDto userProfileDto) {


        UserProfile userProfileDto1 = new UserProfile();
        Optional<User> user = userRepository.findById(userProfileDto.getUserId());

        if (user.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }


        userProfileDto1.setUser(user.get());
        userProfileDto1.setBio(userProfileDto.getBio());
        userProfileDto1.setSkills(userProfileDto.getSkills());
        userProfileDto1.setLearningGoals(userProfileDto.getLearningGoals());
        userProfileDto1.setGithubUsername(userProfileDto.getGithubUsername());
        userProfileDto1.setLinkedinProfile(userProfileDto.getLinkedinProfile());
        userProfileDto1.setPortfolioWebsite(userProfileDto.getPortfolioWebsite());
        userProfileDto1.setProfilePictureUrl(userProfileDto.getProfilePictureUrl());
        userProfileDto1.setContactEmail(userProfileDto.getContactEmail());
        userProfileDto1.setContactPhone(userProfileDto.getContactPhone());
        userProfileDto1.setLocation(userProfileDto.getLocation());
        userProfileDto1.setCertifications(userProfileDto.getCertifications());
        userProfileDto1.setAchievements(userProfileDto.getAchievements());
        userProfileDto1.setCodingStreakDays(userProfileDto.getCodingStreakDays());
        userProfileDto1.setPreferredLanguages(userProfileDto.getPreferredLanguages());
        userProfileDto1.setTimezone(userProfileDto.getTimezone());
        userProfileDto1.setAvailability(userProfileDto.getAvailability());
        userProfileDto1.setExperienceLevel(userProfileDto.getExperienceLevel());

        try {
            UserProfile userProfileDto2 = userProfileRepository.save(userProfileDto1);

            return modelMapper.map(userProfileDto2, UserProfileDto.class);
        } catch (Exception exception) {
            throw new IllegalArgumentException(exception);
        }

    }

    @Override
    public UserProfileDto updateUserProfile(UUID id, UserProfileDto userProfileDto) {
        return null;
    }

    @Override
    public void deleteUserProfile(UUID id) {

    }
}
