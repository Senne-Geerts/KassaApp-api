package be.sennegeerts.kassa_app_api.event.controller;

import be.sennegeerts.kassa_app_api.event.dto.EventRequest;
import be.sennegeerts.kassa_app_api.event.dto.EventResponse;
import be.sennegeerts.kassa_app_api.event.service.EventService;
import jakarta.validation.Valid;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) { this.eventService = eventService; }

    // GET /api/events?activeOnly=true  -> home screen buttons
    @GetMapping
    public List<EventResponse> getAllEvents(@RequestParam(defaultValue = "false") boolean activeOnly) {
        return eventService.findAll(activeOnly);
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
    public ResponseEntity<EventResponse> createEvent(@Valid @RequestBody EventRequest request) {
        EventResponse created = eventService.create(request);
        return ResponseEntity.created(URI.create("/api/events/" + created.id())).body(created);
    }

    @PutMapping("/{id}")
    public EventResponse updateEvent(@PathVariable UUID id, @Valid @RequestBody EventRequest request) {
        return eventService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEvent(@PathVariable UUID id) {
        eventService.delete(id);
    }

    @PostMapping("/{id}/clone")
    public ResponseEntity<EventResponse> cloneEvent(@PathVariable UUID id,
                                                    @Valid @RequestBody EventRequest request) {
        EventResponse created = eventService.cloneEvent(id, request);
        return ResponseEntity.created(URI.create("/api/events/" + created.id())).body(created);
    }
}
