package ru.practicum.statservice.controller;


import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.statservice.Dto.NewEndPointHitDto;
import ru.practicum.statservice.Dto.StatRequestDto;
import ru.practicum.statservice.model.StatsResponce;
import ru.practicum.statservice.service.StatService;

import java.util.Map;

@RestController
@AllArgsConstructor
public class StatController {
    private final StatService service;

    @PostMapping("/hit")
    public Map<String, Long> add(@RequestBody NewEndPointHitDto newDto) {
        return service.add(newDto);
    }


    @GetMapping("/stats")
    public StatsResponce get(@RequestParam(value = "start") String start,
                             @RequestParam(value = "end") String end,
                             @RequestParam(value = "uris", required = false) String[] uris,
                             @RequestParam(value = "unique", required = false) Boolean unique) {
        StatRequestDto statRequestDto = StatRequestDto.builder()
                .start(start)
                .end(end)
                .uris(uris)
                .unique(unique)
                .build();
        return service.get(statRequestDto);
    }
}
