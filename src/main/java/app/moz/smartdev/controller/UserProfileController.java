package app.moz.smartdev.controller;


import app.moz.smartdev.configs.ResponseBuilder;
import app.moz.smartdev.configs.ResponseWrapper;
import app.moz.smartdev.dtos.UserProfileDto;
import app.moz.smartdev.service.UserProfileService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user-profile")
@AllArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;


    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public ResponseWrapper<UserProfileDto> getAllProfiles(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        try {
            List<UserProfileDto> userProfileDtos = userProfileService.getAllUserProfiles();

            int totalCount = userProfileDtos.size();
            int totalPages = (int) Math.ceil((double) totalCount / pageSize);
            return ResponseBuilder.createResponseWrapper(
                    userProfileDtos,
                    pageNumber,
                    pageSize,

                    true,
                    "User profiles retrieved successfully",
                    200
            );

        } catch (Exception e) {
            return ResponseBuilder.createErrorResponse("Error fetching user profiles",
                    null, e.getMessage(), 500);

        }
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResponseWrapper<UserProfileDto> getProfileById(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam UUID userId
    ) {

        try {
            UserProfileDto userProfileDtos = userProfileService.getUserProfileByUserId(userId);
            int totalCount = 1;
            int totalPages = (int) Math.ceil((double) totalCount / pageSize);
            return ResponseBuilder.createResponseWrapper(
                    List.of(userProfileDtos),
                    pageNumber,
                    pageSize,

                    true,
                    "User profiles retrieved successfully",
                    200
            );
        } catch (Exception e) {
            return ResponseBuilder.createErrorResponse("Error fetching user profiles",
                    null, e.getMessage(), 500);
        }


    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseWrapper<UserProfileDto> createProfile(
            @RequestBody UserProfileDto userProfileDto,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        try {
            UserProfileDto userProfileDto1 = userProfileService.createUserProfile(userProfileDto);
            return ResponseBuilder.createResponseWrapper(
                    List.of(userProfileDto1),
                    pageNumber,
                    pageSize,
                    true,
                    "User profile created successfully",
                    201
            );
        } catch (Exception e) {
            return ResponseBuilder.createErrorResponse("Error creating user profile",
                    null, e.getMessage(), 500);
        }
    }

}
