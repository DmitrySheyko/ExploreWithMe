package ru.practicum.mainservice.user.controller.adminController;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.user.dto.UserDto;
import ru.practicum.mainservice.user.service.adminService.UserAdminService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/admin/users")
@Validated
public class UserAdminController {
    private final UserAdminService userAdminService;

    @PostMapping
    public UserDto add(@Valid @RequestBody UserDto user) {
        return userAdminService.add(user);
    }

    @GetMapping
    public List<UserDto> getAll(@RequestParam(name = "from", required = false, defaultValue = "0") @Min(0) int from,
                             @RequestParam(name = "size", required = false, defaultValue = "10") @Min(1) int size) {
        return userAdminService.getAll(from, size);
    }

    @DeleteMapping("/{id}")
    public Map<String, Long> delete(@Positive @PathVariable("id") Long userId) {
        return userAdminService.deleteById(userId);
    }
}
