package be.sennegeerts.kassa_app_api.event.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

import be.sennegeerts.kassa_app_api.event.model.Event;

public record EventRequest(
        @NotBlank @Size(max = 255) String name,
        @NotBlank @Size(max = 255) @Pattern(regexp = Event.SLUG_REGEX, message = "slug: lowercase letters, numbers, dashes only") String slug,
        LocalDate startsAt,
        Boolean active          // null on create → default true
) {}