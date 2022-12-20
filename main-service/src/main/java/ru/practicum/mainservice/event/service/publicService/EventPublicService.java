package ru.practicum.mainservice.event.service.publicService;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.event.dto.EventFullDto;
import ru.practicum.mainservice.event.dto.EventPublicSearchDto;
import ru.practicum.mainservice.event.dto.EventShortDto;
import ru.practicum.mainservice.event.eventMapper.EventMapper;
import ru.practicum.mainservice.event.repository.EventRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class EventPublicService {
    EventRepository repository;
    EventMapper mapper;
//TODO сделать енум возможных полей для сортировки
//    public List<EventShortDto> search(EventPublicSearchDto searchDto, int from, int size, String sort) {
//        int page = from / size;
//        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
//
//    }
}
