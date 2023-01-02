package ru.practicum.statservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.statservice.dto.NewEndPointHitDto;
import ru.practicum.statservice.dto.StatRequestDto;
import ru.practicum.statservice.model.StatsResponse;
import ru.practicum.statservice.service.StatServiceImpl;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class StatController {
    private final StatServiceImpl service;

    @PostMapping("/hit")
    public String add(@RequestBody NewEndPointHitDto newDto) {
        return service.add(newDto);
    }

    @GetMapping("/stats")
    public List<StatsResponse> get(@RequestParam(value = "start") String start,
                                   @RequestParam(value = "end") String end,
                                   @RequestParam(value = "uris", required = false) List<String> uris,
                                   @RequestParam(value = "unique", required = false, defaultValue = "false")
                                   String unique) {
        StatRequestDto statRequestDto = StatRequestDto.builder()
                .start(URLDecoder.decode(start, StandardCharsets.UTF_8))
                .end(URLDecoder.decode(end, StandardCharsets.UTF_8))
                .uris(uris.stream()
                        .map(uri -> URLDecoder.decode(uri, StandardCharsets.UTF_8))
                        .collect(Collectors.toList()))
                .unique(Boolean.valueOf(URLDecoder.decode(unique, StandardCharsets.UTF_8)))
                .build();
        return service.get(statRequestDto);
    }
}