package ru.practicum.statservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.statservice.dto.NewEndPointHitDto;
import ru.practicum.statservice.dto.StatRequestDto;
import ru.practicum.statservice.model.StatsRequest;
import ru.practicum.statservice.mapper.StatMapper;
import ru.practicum.statservice.model.EndPointHit;
import ru.practicum.statservice.model.StatsResponse;
import ru.practicum.statservice.repository.StatRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Class of service for {@link EndPointHit} and {@link StatsResponse} entities.
 * implements interface {@link StatService}
 *
 * @author DmitrySheyko
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class StatServiceImpl implements StatService {
    private final StatRepository repository;

    @Override
    public String add(NewEndPointHitDto newDto) {
        EndPointHit endPointHit = StatMapper.toEndPointHit(newDto);
        if (endPointHit.getTimeStamp() == null) {
            endPointHit.setTimeStamp(LocalDateTime.now());
        }
        endPointHit = repository.save(endPointHit);
        log.info("New EndPointHit id={} successfully add", endPointHit.getId());
        return String.format("Successfully add new endPointHit id=%s", endPointHit.getId());
    }

    @Override
    public List<StatsResponse> get(StatRequestDto requestDto) {
        StatsRequest statsRequest = StatMapper.toStatRequest(requestDto);
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