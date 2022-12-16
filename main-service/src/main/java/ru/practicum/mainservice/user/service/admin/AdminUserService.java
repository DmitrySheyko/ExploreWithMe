package ru.practicum.mainservice.user.service.admin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.user.dto.UserDto;
import ru.practicum.mainservice.user.mapper.UserMapper;
import ru.practicum.mainservice.user.repository.UserRepository;
import ru.practicum.mainservice.user.model.User;

import java.util.List;
import java.util.Map;


@Service
@Slf4j
@AllArgsConstructor
public class AdminUserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    public UserDto add(UserDto userDto){
        User user = mapper.toEntity(userDto);
        repository.save(user);
        log.info("Пользователь userId={} успешно добавлен", userDto.getId());
        return mapper.toDto(user);
    }

    public List<UserDto> getAll(){
        return repository.findAll();
    }

    public Map<String, Long> delete (Long userId){
        repository.deleteById(userId);
        return Map.of("Успешно удален пользователь id=", userId);
    }
}
