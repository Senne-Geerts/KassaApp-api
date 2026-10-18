package be.sennegeerts.kassa_app_api.event.service;

import be.sennegeerts.kassa_app_api.event.dto.EventRequest;
import be.sennegeerts.kassa_app_api.event.dto.EventResponse;
import be.sennegeerts.kassa_app_api.event.model.Event;
import be.sennegeerts.kassa_app_api.event.repo.EventRepository;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
@Service
@Transactional
public class EventService {
    private final EventRepository events;

    public EventService(EventRepository events) { this.events = events; }

    @Transactional(readOnly = true)
    public List<EventResponse> findAll(boolean activeOnly) {
        List<Event> list = activeOnly
                ? events.findByActiveTrueOrderByStartsAtDesc()
                : events.findAll();
        return list.stream().map(EventResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public EventResponse findById(UUID id) {
        return EventResponse.from(getOrThrow(id));
    }

    @Transactional(readOnly = true)
    public EventResponse findBySlug(String slug) {
        return events.findBySlug(slug)
                .map(EventResponse::from)
                .orElseThrow(() -> new EntityNotFoundException("Event not found: " + slug));
    }

    public EventResponse create(EventRequest request) {
        if (events.existsBySlug(request.slug())) {
            throw slugTaken(request.slug());
        }
        return EventResponse.from(events.save(
                new Event(request.name(), request.slug(), request.startsAt())));
    }

    public EventResponse update(UUID id, EventRequest request) {
        Event event = getOrThrow(id);
        if (events.existsBySlugAndIdNot(request.slug(), id)) {
            throw slugTaken(request.slug());
        }
        event.setName(request.name());
        event.setSlug(request.slug());
        event.setStartsAt(request.startsAt());
        event.setActive(request.active());
        return EventResponse.from(event); // managed entity, no save() needed
    }

    public void delete(UUID id) {
        events.delete(getOrThrow(id));
    }

    private Event getOrThrow(UUID id) {
        return events.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event not found: " + id));
    }

    private ResponseStatusException slugTaken(String slug) {
        return new ResponseStatusException(HttpStatus.CONFLICT, "Slug already in use: " + slug);
    }
}
