package ru.practicum.mainservice.user.service;

import ru.practicum.mainservice.user.dto.UserDto;
import ru.practicum.mainservice.user.model.User;

import java.util.List;

/**
 * Interface of service class for {@link User} entity
 *
 * @author DmitrySheyko
 */
public interface UserService {

    UserDto add(UserDto userDto);

    List<UserDto> getAllById(List<Long> ids, int from, int size);

    String deleteById(Long userId);
}