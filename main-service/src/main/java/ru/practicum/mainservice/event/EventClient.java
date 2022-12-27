package ru.practicum.mainservice.event;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.practicum.mainservice.event.dto.NewEndPointHit;
import ru.practicum.mainservice.event.dto.StatsResponseDto;
import ru.practicum.mainservice.exceptions.NotFoundException;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
@AllArgsConstructor
public class EventClient {
    private static final DateTimeFormatter DATE_TIME_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void addEndPointHit(String ip, String uri, LocalDateTime timeStamp) {
        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = "http://localhost:9090/hit";
        NewEndPointHit newDto = NewEndPointHit.builder()
                .uri(uri)
                .ip(ip)
                .timeStamp(dateTimeToSting(timeStamp))
                .build();
        System.out.println("Вот что отправили в статистику: " + newDto);
        HttpEntity<NewEndPointHit> request = new HttpEntity<>(newDto);
        String response = restTemplate.postForObject(resourceUrl, request, String.class);
        System.out.println("Вот что пришло: " + response);
        if (response == null || response.isBlank() || !response.toLowerCase().contains("successfully".toLowerCase())) {
            log.warn("Statistic don't updated");
        } else {
            log.info("Statistic successfully updated");
        }
    }

    public Integer getViews(Long eventId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:9090/stats";
        StringBuilder param = new StringBuilder("?");
        param.append("start=" + URLEncoder.encode(dateTimeToSting(LocalDateTime.of(2020, 1, 1, 0, 0, 1)), StandardCharsets.UTF_8) + "&");
        param.append("end=" + URLEncoder.encode(dateTimeToSting(LocalDateTime.now().plusHours(1L)), StandardCharsets.UTF_8) + "&");
        param.append("uris=" + URLEncoder.encode(("/events/" + eventId), StandardCharsets.UTF_8) + "&");
        param.append("unique=" + URLEncoder.encode("false", StandardCharsets.UTF_8));
        String resourceUrl = url + param;
        System.out.println("Url: " + resourceUrl);
        System.out.println("Url декодированый : " + URLDecoder.decode(resourceUrl, StandardCharsets.UTF_8));
        ResponseEntity<StatsResponseDto[]> responseEntity =
                restTemplate.getForEntity(resourceUrl, StatsResponseDto[].class);
        if (responseEntity == null) {
            throw new NotFoundException(String.format("Gotten null value of view statistic from stat-service for " +
                    "event id=%s", eventId));
        }
        StatsResponseDto[] result = responseEntity.getBody();
        if (result.length == 0) {
            return 0;
        }
        StatsResponseDto statsResponseDto = result[0];
        log.info("Successfully received views for event id={}, views={}", eventId, statsResponseDto.getHits());
        return statsResponseDto.getHits();
    }

    private String dateTimeToSting(LocalDateTime dateTime) {
        return dateTime.format(DATE_TIME_PATTERN);
    }
}
