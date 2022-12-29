package ru.practicum.mainservice.user.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.exceptions.NotFoundException;
import ru.practicum.mainservice.user.dto.UserDto;
import ru.practicum.mainservice.user.mapper.UserMapper;
import ru.practicum.mainservice.user.model.User;
import ru.practicum.mainservice.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    @Override
    public UserDto add(UserDto userDto) {
        User user = mapper.toEntity(userDto);
        user = repository.save(user);
        log.info("User id={} successfully add", user.getId());
        return mapper.toDto(user);
    }

    @Override
    public User findById(Long userId) {
        Optional<User> optionalUser = repository.findById(userId);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }
        log.warn("Information about user id={} is empty", userId);
        throw new NotFoundException((String.format("User with id=%s was not found.", userId)));
    }

    @Override
    public List<UserDto> getAllById(List<Long> ids, int from, int size) {
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        Page<User> usersPage = repository.findAllByIdIn(ids, pageable);
        List<UserDto> usersList = usersPage.stream().map(mapper::toDto).collect(Collectors.toList());
        log.info("List of users successfully received, page: {}, size: {}", page, size);
        return usersList;
    }

    @Override
    public String deleteById(Long userId) {
        checkIsObjectInStorage(userId);
        repository.deleteById(userId);
        log.info("User id={} successfully deleted", userId);
        return String.format("Successfully deleted user id=%s", userId);
    }

    @Override
    public void checkIsObjectInStorage(Long userId) {
        if (!repository.existsById(userId)) {
            log.warn("User id={} not found", userId);
            throw new NotFoundException((String.format("User with id=%s was not found.", userId)));
        }
    }
}