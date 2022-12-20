package ru.practicum.mainservice.category.controller.publicController;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.mainservice.category.dto.CategoryDto;
import ru.practicum.mainservice.category.service.publicService.CategoryPublicService;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/categories")
@AllArgsConstructor
public class CategoryPublicController {
    CategoryPublicService categoryPublicService;

    @GetMapping
    public List<CategoryDto> getAll(@RequestParam(name = "from", required = false, defaultValue = "0") @Min(0) int from,
                                    @RequestParam(name = "size", required = false, defaultValue = "10") @Min(1) int size) {
        return categoryPublicService.getAll(from, size);
    }

    @GetMapping("/{id}")
    public CategoryDto getById (@RequestParam("id") Long categoryId){
        return categoryPublicService.getById(categoryId);
    }
}
