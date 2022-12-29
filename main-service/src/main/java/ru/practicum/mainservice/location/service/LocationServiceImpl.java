package ru.practicum.mainservice.location.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.location.model.Location;
import ru.practicum.mainservice.location.repository.LocationRepository;

@Service
@Slf4j
@AllArgsConstructor
public class LocationServiceImpl implements LocationService {
    LocationRepository repository;

    @Override
    public Location save(Location location) {
        location = repository.save(location);
        log.info("Location id={} successfully add", location.getId());
        return location;
    }
}