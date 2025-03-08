package app.moz.smartdev.controller;

import app.moz.smartdev.configs.ResponseBuilder;
import app.moz.smartdev.configs.ResponseWrapper;
import app.moz.smartdev.dtos.UserDto;
import app.moz.smartdev.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public ResponseWrapper<UserDto> getAllUsers(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize) {
        try {
            List<UserDto> userDtos = userService.findAll();
            int totalCount = userDtos.size();
            int totalPages = (int) Math.ceil((double) totalCount / pageSize);
            return ResponseBuilder.createResponseWrapper(
                    userDtos,
                    pageNumber,
                    pageSize,
                    totalCount,
                    totalPages,
                    true,
                    "Users retrieved successfully",
                    200);
        } catch (Exception e) {
            return ResponseBuilder.createErrorResponse("Error fetching users",
                    null, e.getMessage(), 500);
        }
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody UserDto userDto) {
        return userService.create(userDto);
    }
}