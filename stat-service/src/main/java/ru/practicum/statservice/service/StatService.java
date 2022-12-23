package ru.practicum.statservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.statservice.Dto.NewEndPointHitDto;
import ru.practicum.statservice.Dto.StatRequestDto;
import ru.practicum.statservice.Dto.StatsRequest;
import ru.practicum.statservice.mapper.StatMapper;
import ru.practicum.statservice.model.EndPointHit;
import ru.practicum.statservice.model.StatsResponce;
import ru.practicum.statservice.repository.StatRepository;

import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
public class StatService {
    private final StatRepository repository;
    private final StatMapper mapper;

    public Map<String, Long> add(NewEndPointHitDto newDto) {
        EndPointHit endPointHit = mapper.toEndPointHit(newDto);
        endPointHit = repository.save(endPointHit);
        log.info("New EndPointHit id={} successfully add", endPointHit.getId());
        return Map.of("Successfully add new endPointHit id=", endPointHit.getId());
    }

    public StatsResponce get(StatRequestDto requestDto) {
        StatsRequest statsRequest = mapper.toStatRequest(requestDto);
        StatsResponce statsResponce = null;
        if (requestDto.getUris() == null && requestDto.getUnique() == null) {
            statsResponce = repository.findByPeriod(statsRequest.getStart(), statsRequest.getEnd());
        }
        if (requestDto.getUris() != null && requestDto.getUnique() == null) {
            statsResponce = repository.findByPeriodAndUris(statsRequest.getStart(), statsRequest.getEnd(), statsRequest.getUris());
        }
        if (requestDto.getUris() == null && requestDto.getUnique() != null) {
            statsResponce = repository.findByPeriodAndUnique(statsRequest.getStart(), statsRequest.getEnd(), statsRequest.getUnique());
        }
        if (requestDto.getUris() != null && requestDto.getUnique() != null) {
            statsResponce = repository.findByPeriodAndUrisAndUnique(statsRequest.getStart(), statsRequest.getEnd(),
                    statsRequest.getUris(), statsRequest.getUnique());
        }
        log.info("StatResponce successfully received");
        return statsResponce;
    }

}
