package be.sennegeerts.kassa_app_api.event.controller;

import be.sennegeerts.kassa_app_api.event.dto.CreateEventRequest;
import be.sennegeerts.kassa_app_api.event.dto.EventResponse;
import be.sennegeerts.kassa_app_api.event.dto.UpdateEventRequest;
import be.sennegeerts.kassa_app_api.event.service.EventService;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public List<EventResponse> getAllEvents() {
        return eventService.findAll();
    }

    @GetMapping("/{id}")
    public EventResponse getEventById(@PathVariable UUID id) {
        return eventService.findById(id);
    }

    @GetMapping("/slug/{slug}")
    public EventResponse getEventBySlug(@PathVariable String slug) {
        return eventService.findBySlug(slug);
    }

    @PostMapping
    public EventResponse createEvent(@RequestBody CreateEventRequest request) {
        return eventService.create(request);
    }

    @PutMapping("/{id}")
    public EventResponse updateEvent(@PathVariable UUID id, @RequestBody UpdateEventRequest request) {
        return eventService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable UUID id) {
        eventService.delete(id);
    }
}
