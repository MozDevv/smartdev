package app.moz.smartdev.service;

import app.moz.smartdev.dtos.UserDto;
import app.moz.smartdev.entity.Role;
import app.moz.smartdev.entity.User;
import app.moz.smartdev.repository.RoleRepository;
import app.moz.smartdev.repository.UserRepository;
import lombok.AllArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final RoleRepository roleRepository;


    @Override
    public List<UserDto> findAll() {

        return userRepository.findAll().stream()
                .map((user -> modelMapper.map(user, UserDto.class)))
                .toList();
    }

    public String activateAccount(String activationCode) {
        return userRepository.findByActivationCode(activationCode)
                .map(user -> {
                    user.setStatus("ACTIVE");
                    userRepository.save(user);
                    return "Account activated successfully";
                })
                .orElse("Account not found");
    }


    @Override
    public UserDto findById(String id) {
        return null;
    }

    @Override
    public UserDto findByEmail(String email) {
        return null;
    }

    @Override
    public UserDto create(UserDto userDto) {

        User user = new User();

        user.setEmail(userDto.getEmail());
        user.setUsername(userDto.getUsername());
        // user.setPassword(userDto.getPassword());
        user.setProfilePicture(userDto.getProfilePicture());
        user.setStatus(userDto.getStatus());

        if (userDto.getRoleId() != null) {
            Optional<Role> role = roleRepository.findById(userDto.getRoleId());
            if (role.isEmpty()) {
                throw new IllegalArgumentException("Role not found");
            }
            user.getRoles().add(role.get());
        }
        try {
            User user1 = userRepository.save(user);
            return modelMapper.map(user1, UserDto.class);
        } catch (Exception exception) {
            throw new IllegalArgumentException(exception);
        }


    }

    @Override
    public UserDto update(UserDto userDto) {
        return null;
    }

    @Override
    public void delete(String id) {
    }
}