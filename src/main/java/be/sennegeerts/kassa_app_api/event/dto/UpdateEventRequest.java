package be.sennegeerts.kassa_app_api.event.dto;

import java.time.LocalDate;

public record UpdateEventRequest(
        String name,
        String slug,
        LocalDate startsAt,
        boolean active
) {
}
