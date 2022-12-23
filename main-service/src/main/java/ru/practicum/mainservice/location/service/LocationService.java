package ru.practicum.mainservice.location.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.exceptions.NotFoundException;
import ru.practicum.mainservice.location.model.Location;
import ru.practicum.mainservice.location.repository.LocationRepository;

import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class LocationService {
    LocationRepository repository;

    public Location save(Location location) {
        return repository.save(location);
    }

    public Location findById(Long locationId) {
        Optional<Location> optionalLocation = repository.findById(locationId);
        if (optionalLocation.isPresent()) {
            return optionalLocation.get();
        }
        log.warn("Information about location id={} is empty", locationId);
        throw new NotFoundException((String.format("Location with id=%s was not found.", locationId)),
                "The required object was not found.");
    }
}
