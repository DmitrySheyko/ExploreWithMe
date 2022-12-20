package ru.practicum.mainservice.event.service.adminService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.event.dto.AdminUpdateEventDto;
import ru.practicum.mainservice.event.dto.EventAdminSearch;
import ru.practicum.mainservice.event.dto.EventAdminSearchDto;
import ru.practicum.mainservice.event.dto.EventFullDto;
import ru.practicum.mainservice.event.eventMapper.EventMapper;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.event.model.State;
import ru.practicum.mainservice.event.service.EventService;
import ru.practicum.mainservice.exceptions.ValidationException;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class EventAdminService {
    private final EventService service;
    private final EventMapper mapper;

    public EventFullDto update(AdminUpdateEventDto eventDto) {
        service.checkIsObjectInStorage(eventDto.getEventId());
        Event event = mapper.toEvent(eventDto);
        event = service.update(event);
        EventFullDto eventFullDto = mapper.toFullDto(event);
        log.info("Event eventId={} successfully updated", event.getId());
        return eventFullDto;
    }

    public EventFullDto publish(Long eventId) {
        service.checkIsObjectInStorage(eventId);
        Event event = service.findById(eventId);
        if(! event.getState().equals(State.PENDING)){
            throw new ValidationException("For publication event should be in status PENDING",
                    "For the requested operation the conditions are not met");
        }
        service.checkEventDateForPublish(event.getEventDate());
        event.setState(State.PUBLISHED);
        EventFullDto eventFullDto = mapper.toFullDto(event);
        log.info("Event eventId={} successfully published", event.getId());
        return eventFullDto;
    }

    public EventFullDto reject(Long eventId) {
        service.checkIsObjectInStorage(eventId);
        Event event = service.findById(eventId);
        if(event.getState().equals(State.PUBLISHED)){
            throw new ValidationException("Published event can't be rejected",
                    "For the requested operation the conditions are not met");
        }
        event.setState(State.CANCELED);
        EventFullDto eventFullDto = mapper.toFullDto(event);
        log.info("Event eventId={} successfully canceled", event.getId());
        return eventFullDto;
    }

    public List<EventFullDto> search(EventAdminSearchDto searchDto, int from, int size){
        EventAdminSearch eventSearch = mapper.
    }
}
