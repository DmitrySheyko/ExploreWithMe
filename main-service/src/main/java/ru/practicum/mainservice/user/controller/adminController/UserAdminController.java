package ru.practicum.mainservice.user.controller.adminController;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.user.dto.UserDto;
import ru.practicum.mainservice.user.model.User;
import ru.practicum.mainservice.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.util.List;

/**
 * Class of controller for {@link User} entity for users with Admin role
 *
 * @author DmitrySheyko
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
@Validated
public class UserAdminController {
    private final UserService service;

    @PostMapping
    public UserDto add(@Valid @RequestBody UserDto user) {
        return service.add(user);
    }

    @GetMapping
    public List<UserDto> getAllById(@RequestParam(name = "ids") List<Long> ids,
                                    @RequestParam(name = "from", defaultValue = "0") @Min(0) int from,
                                    @RequestParam(name = "size", defaultValue = "10") @Min(1) int size) {
        return service.getAllById(ids, from, size);
    }

    @DeleteMapping("/{id}")
    public String delete(@Positive @PathVariable("id") Long userId) {
        return service.deleteById(userId);
    }
}