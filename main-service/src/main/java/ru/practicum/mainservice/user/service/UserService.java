package ru.practicum.mainservice.user.service;

import ru.practicum.mainservice.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto add(UserDto userDto);

    List<UserDto> getAllById(List<Long> ids, int from, int size);

    String deleteById(Long userId);
}