package be.sennegeerts.kassa_app_api.event.service;

import be.sennegeerts.kassa_app_api.event.dto.CreateEventRequest;
import be.sennegeerts.kassa_app_api.event.dto.EventResponse;
import be.sennegeerts.kassa_app_api.event.dto.UpdateEventRequest;
import be.sennegeerts.kassa_app_api.event.model.Event;
import be.sennegeerts.kassa_app_api.event.repo.EventRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EventService {
    private final EventRepository events;

    public EventService(EventRepository events) {
        this.events = events;
    }

    public List<EventResponse> findAll() {
        return events.findAll().stream()
                .map(EventResponse::from)
                .toList();
    }

    public EventResponse findById(UUID id) {
        return events.findById(id)
                .map(EventResponse::from)
                .orElseThrow(() -> new EntityNotFoundException("Event not found: " + id));
    }

    public EventResponse findBySlug(String slug) {
        return events.findBySlug(slug)
                .map(EventResponse::from)
                .orElseThrow(() -> new EntityNotFoundException("Event not found: " + slug));
    }

    public EventResponse create(CreateEventRequest request) {
        Event event = new Event(request.name(), request.slug(), request.startsAt());
        return EventResponse.from(events.save(event));
    }

    public EventResponse update(UUID id, UpdateEventRequest request) {
        Event event = events.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event not found: " + id));
        event.setName(request.name());
        event.setSlug(request.slug());
        event.setStartsAt(request.startsAt());
        event.setActive(request.active());
        return EventResponse.from(events.save(event));
    }

    public void delete(UUID id) {
        events.delete(events.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event not found: " + id)));
    }

}
