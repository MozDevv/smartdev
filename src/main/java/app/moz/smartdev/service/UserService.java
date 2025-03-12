package app.moz.smartdev.service;

import app.moz.smartdev.dtos.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> findAll();

    UserDto findById(String id);

    UserDto findByEmail(String email);

    UserDto create(UserDto userDto);

    UserDto update(UserDto userDto);

    void delete(String id);

    String activateAccount(String activationCode);


}
