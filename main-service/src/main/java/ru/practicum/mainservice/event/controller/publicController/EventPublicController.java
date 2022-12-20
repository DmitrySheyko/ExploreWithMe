package ru.practicum.mainservice.event.controller.publicController;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.mainservice.event.dto.EventPublicSearchDto;
import ru.practicum.mainservice.event.dto.EventShortDto;
import ru.practicum.mainservice.event.service.publicService.EventPublicService;

import java.util.List;

@RestController
@RequestMapping("/events")
@AllArgsConstructor
public class EventPublicController {
    EventPublicService service;

//    @GetMapping
//    public List<EventShortDto> search(@RequestParam("text") String text,
//                                      @RequestParam("categories") Integer[] categories,
//                                      @RequestParam("paid") Boolean paid,
//                                      @RequestParam("rangeStart") String rangeStart,
//                                      @RequestParam("rangeEnd") String rangeEnd,
//                                      @RequestParam("onlyAvailable") Boolean onlyAvailable,
//                                      @RequestParam("sort") String sort,
//                                      @RequestParam("from") Integer from,
//                                      @RequestParam("size") Integer size) {
//        EventPublicSearchDto searchDto = EventPublicSearchDto.builder()
//                .text(text)
//                .categories(categories)
//                .paid(paid)
//                .rangeStart(rangeStart)
//                .rangeEnd(rangeEnd)
//                .onlyAvailable(onlyAvailable)
//                .build();
//        return service.serach(searchDto, from, size, sort);
//    }
}
