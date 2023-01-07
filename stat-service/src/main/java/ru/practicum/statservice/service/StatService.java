package ru.practicum.statservice.service;

import ru.practicum.statservice.dto.NewEndPointHitDto;
import ru.practicum.statservice.dto.StatRequestDto;
import ru.practicum.statservice.model.StatsResponse;

import java.util.List;

public interface StatService {

    String add(NewEndPointHitDto newDto);

    List<StatsResponse> get(StatRequestDto requestDto);
}
