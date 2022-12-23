package ru.practicum.mainservice.user.service.adminService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.user.dto.UserDto;
import ru.practicum.mainservice.user.mapper.UserMapper;
import ru.practicum.mainservice.user.model.User;
import ru.practicum.mainservice.user.service.UserService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@Slf4j
@AllArgsConstructor
public class UserAdminService {
    private final UserService service;
    private final UserMapper mapper;

    public UserDto add(UserDto userDto) {
        User user = mapper.toEntity(userDto);
        user = service.save(user);
        log.info("User id={} successfully add", user.getId());
        return mapper.toDto(user);
    }

    public List<UserDto> getAll(int from, int size) {
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        Page<User> usersPage = service.findAll(pageable);
        List<UserDto> usersList = usersPage.stream().map(mapper::toDto).collect(Collectors.toList());
        log.info("List of users successfully received , page: {}, size: {}", page, size);
        return usersList;
    }

    public Map<String, Long> deleteById(Long userId) {
        service.checkIsObjectInStorage(userId);
        Map<String, Long> response = service.deleteById(userId);
        log.info("User id={} successfully deleted", userId);
        return response;
    }
}

