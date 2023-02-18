package ru.practicum.statservice.service;

import ru.practicum.statservice.dto.NewEndPointHitDto;
import ru.practicum.statservice.dto.StatRequestDto;
import ru.practicum.statservice.model.EndPointHit;
import ru.practicum.statservice.model.StatsResponse;

import java.util.List;

/**
 * Interface of service class for {@link EndPointHit} and {@link StatsResponse} entities.
 *
 * @author DmitrySheyko
 */
public interface StatService {

    String add(NewEndPointHitDto newDto);

    List<StatsResponse> get(StatRequestDto requestDto);
}
