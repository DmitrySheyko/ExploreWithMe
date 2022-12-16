package ru.practicum.mainservice.user.controller.admin;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.user.dto.UserDto;
import ru.practicum.mainservice.user.service.admin.AdminUserService;
import ru.practicum.mainservice.user.model.User;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/admin/users")
@Validated
public class UserController {
    private final AdminUserService adminUserService;

    @PostMapping
    public UserDto add(@Valid @RequestBody UserDto user) {
        return adminUserService.add(user);
    }

    @GetMapping
    public List<User> getAll() {
        return adminUserService.getAll();
    }

    @DeleteMapping("/{id}")
    public Map<String, Long> delete(@Positive @PathVariable("id") Long userId) {
        return adminUserService.delete(userId);
    }
}
