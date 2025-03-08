package app.moz.smartdev.service;

import app.moz.smartdev.dtos.UserProfileDto;

import java.util.List;
import java.util.UUID;

public interface UserProfileService {

    List<UserProfileDto> getAllUserProfiles();



    UserProfileDto getUserProfileByUserId(UUID userId);

    UserProfileDto createUserProfile(UserProfileDto userProfileDto);

    UserProfileDto updateUserProfile(UUID id, UserProfileDto userProfileDto);

    void deleteUserProfile(UUID id);

}
