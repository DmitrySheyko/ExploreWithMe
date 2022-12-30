package ru.practicum.statservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.statservice.dto.NewEndPointHitDto;
import ru.practicum.statservice.dto.StatRequestDto;
import ru.practicum.statservice.dto.StatsRequest;
import ru.practicum.statservice.mapper.StatMapper;
import ru.practicum.statservice.model.EndPointHit;
import ru.practicum.statservice.model.StatsResponse;
import ru.practicum.statservice.repository.StatRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class StatServiceImpl implements StatService {
    private final StatRepository repository;
    private final StatMapper mapper;

    @Override
    public String add(NewEndPointHitDto newDto) {
        EndPointHit endPointHit = mapper.toEndPointHit(newDto);
        if (endPointHit.getTimeStamp() == null) {
            endPointHit.setTimeStamp(LocalDateTime.now());
        }
        endPointHit = repository.save(endPointHit);
        log.info("New EndPointHit id={} successfully add", endPointHit.getId());
        return String.format("Successfully add new endPointHit id=%s", endPointHit.getId());
    }

    @Override
    public List<StatsResponse> get(StatRequestDto requestDto) {
        StatsRequest statsRequest = mapper.toStatRequest(requestDto);
        List<StatsResponse> statsResponse = null;
        if (statsRequest.getUris() == null && !requestDto.getUnique()) {
            statsResponse = repository.findByPeriod(statsRequest.getStart(), statsRequest.getEnd());
        }
        if (statsRequest.getUris() != null && !requestDto.getUnique()) {
            statsResponse = repository.findByPeriodAndUris(statsRequest.getStart(), statsRequest.getEnd(),
                    statsRequest.getUris());
        }
        if (statsRequest.getUris() == null && requestDto.getUnique()) {
            statsResponse = repository.findByPeriodAndUnique(statsRequest.getStart(), statsRequest.getEnd());
        }
        if (statsRequest.getUris() != null && requestDto.getUnique()) {
            statsResponse = repository.findByPeriodAndUrisAndUnique(statsRequest.getStart(), statsRequest.getEnd(),
                    statsRequest.getUris());
        }
        log.info("Statistic successfully sent from stat-service");
        return statsResponse;
    }
}