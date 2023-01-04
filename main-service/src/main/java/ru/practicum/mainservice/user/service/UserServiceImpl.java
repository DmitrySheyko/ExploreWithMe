package ru.practicum.mainservice.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.user.dto.UserDto;
import ru.practicum.mainservice.user.mapper.UserMapper;
import ru.practicum.mainservice.user.model.User;
import ru.practicum.mainservice.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    @Transactional
    public UserDto add(UserDto userDto) {
        User user = UserMapper.toEntity(userDto);
        user = repository.save(user);
        log.info("User id={} successfully add", user.getId());
        return UserMapper.toDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAllById(List<Long> ids, int from, int size) {
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        Page<User> usersPage = repository.findAllByIdIn(ids, pageable);
        List<UserDto> usersList = usersPage.stream().map(UserMapper::toDto).collect(Collectors.toList());
        log.info("List of users successfully received, page: {}, size: {}", page, size);
        return usersList;
    }

    @Override
    @Transactional
    public String deleteById(Long userId) {
        repository.deleteById(userId);
        log.info("User id={} successfully deleted", userId);
        return String.format("Successfully deleted user id=%s", userId);
    }
}