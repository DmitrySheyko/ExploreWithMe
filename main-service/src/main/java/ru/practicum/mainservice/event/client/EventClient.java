package ru.practicum.mainservice.event.client;

import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.practicum.mainservice.event.dto.NewEndPointHit;
import ru.practicum.mainservice.event.dto.StatsResponseDto;
import ru.practicum.mainservice.exceptions.NotFoundException;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
public class EventClient {

    private static String STAT_SERVER_URL;
    private static final DateTimeFormatter DATE_TIME_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final LocalDateTime START_TIME = LocalDateTime.of(2020, 1, 1, 0, 0, 1);

    @Value("${STAT_SERVER_URL}")
    public void setStatServerUrl(String statServerUrl) {
        EventClient.STAT_SERVER_URL = statServerUrl;
    }

    public void addEndPointHit(String ip, String uri, LocalDateTime timeStamp) {
        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = STAT_SERVER_URL + "/hit";
        NewEndPointHit newDto = NewEndPointHit.builder()
                .uri(uri)
                .ip(ip)
                .timeStamp(dateTimeToSting(timeStamp))
                .build();
        HttpEntity<NewEndPointHit> request = new HttpEntity<>(newDto);
        String response = restTemplate.postForObject(resourceUrl, request, String.class);
        if (response == null || !response.toLowerCase().contains("successfully".toLowerCase())) {
            log.warn("Statistic don't updated");
        } else {
            log.info("Statistic successfully updated");
        }
    }

    public static Integer getViews(Long eventId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = STAT_SERVER_URL + "/stats";
        StringBuilder param = new StringBuilder("?");
        param.append("start=" + URLEncoder.encode(dateTimeToSting(START_TIME), StandardCharsets.UTF_8) + "&");
        param.append("end=" + URLEncoder.encode(dateTimeToSting(LocalDateTime.now()), StandardCharsets.UTF_8) + "&");
        param.append("uris=" + URLEncoder.encode(("/events/" + eventId), StandardCharsets.UTF_8) + "&");
        param.append("unique=" + URLEncoder.encode("false", StandardCharsets.UTF_8));
        String resourceUrl = url + param;
        log.info("Prepared URL for stat request, URL={}", resourceUrl);
        ResponseEntity<StatsResponseDto[]> responseEntity =
                restTemplate.getForEntity(resourceUrl, StatsResponseDto[].class);
        StatsResponseDto[] result = responseEntity.getBody();
        if (result == null) {
            throw new NotFoundException(String.format("Gotten null value of view statistic from stat-service for " +
                    "event id=%s", eventId));
        }
        if (result.length == 0) {
            return 0;
        }
        StatsResponseDto statsResponseDto = result[0];
        log.info("Successfully received views for event id={}, views={}", eventId, statsResponseDto.getHits());
        return statsResponseDto.getHits();
    }

    private static String dateTimeToSting(LocalDateTime dateTime) {
        return dateTime.format(DATE_TIME_PATTERN);
    }
}