package be.sennegeerts.kassa_app_api.event.dto;

import be.sennegeerts.kassa_app_api.event.model.Event;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record EventResponse(
        UUID id,
        String name,
        String slug,
        LocalDate startsAt,
        boolean active,
        Instant createdAt
) {
    public static EventResponse from(Event event) {
        return new EventResponse(
                event.getId(),
                event.getName(),
                event.getSlug(),
                event.getStartsAt(),
                event.isActive(),
                event.getCreatedAt()
        );
    }
}
