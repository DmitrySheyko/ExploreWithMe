package ru.practicum.mainservice.user.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.exceptions.NotFoundException;
import ru.practicum.mainservice.user.model.User;
import ru.practicum.mainservice.user.repository.UserRepository;

import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {
    UserRepository repository;

    public User save(User user) {
        return repository.save(user);
    }

    public Page<User> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public User findById(Long userId) {
        Optional<User> optionalUser = repository.findById(userId);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }
        log.warn("Information about user id={} is empty", userId);
        throw new NotFoundException((String.format("User with id=%s was not found.", userId)),
                "The required object was not found.");
    }

    public Map<String, Long> deleteById(Long userId) {
        repository.deleteById(userId);
        return Map.of("Successfully deleted user id=", userId);
    }

    public void checkIsObjectInStorage(User user) {
        if (!repository.existsById(user.getId())) {
            log.warn("User id={} not found", user.getId());
            throw new NotFoundException((String.format("User with id=%s was not found.", user.getId())),
                    "The required object was not found.");
        }
    }

    public void checkIsObjectInStorage(Long userId) {
        if (!repository.existsById(userId)) {
            log.warn("User id={} not found", userId);
            throw new NotFoundException((String.format("User with id=%s was not found.", userId)),
                    "The required object was not found.");
        }
    }
}
