package be.sennegeerts.kassa_app_api.event.dto;

import java.time.LocalDate;

public record CreateEventRequest(
        String name,
        String slug,
        LocalDate startsAt
) {
}
