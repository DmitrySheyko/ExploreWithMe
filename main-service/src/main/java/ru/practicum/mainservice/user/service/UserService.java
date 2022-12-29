package ru.practicum.mainservice.user.service;

import ru.practicum.mainservice.user.dto.UserDto;
import ru.practicum.mainservice.user.model.User;

import java.util.List;

public interface UserService {

    UserDto add(UserDto userDto);

    User findById(Long userId);

    List<UserDto> getAllById(List<Long> ids, int from, int size);

    String deleteById(Long userId);

    void checkIsObjectInStorage(Long userId);
}
