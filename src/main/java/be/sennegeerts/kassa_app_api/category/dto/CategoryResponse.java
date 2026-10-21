package be.sennegeerts.kassa_app_api.category.dto;

import java.util.UUID;

import be.sennegeerts.kassa_app_api.category.model.Category;

public record CategoryResponse(UUID id, String name, int sortOrder) {
    public static CategoryResponse from(Category c) {
        return new CategoryResponse(c.getId(), c.getName(), c.getSortOrder());
    }
}
