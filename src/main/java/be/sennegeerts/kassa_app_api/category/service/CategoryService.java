package be.sennegeerts.kassa_app_api.category.service;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import be.sennegeerts.kassa_app_api.category.dto.CategoryRequest;
import be.sennegeerts.kassa_app_api.category.dto.CategoryResponse;
import be.sennegeerts.kassa_app_api.category.model.Category;
import be.sennegeerts.kassa_app_api.event.model.Event;
import be.sennegeerts.kassa_app_api.category.repo.CategoryRepository;
import be.sennegeerts.kassa_app_api.event.repo.EventRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional 
public class CategoryService {
    private final CategoryRepository categories;
    private final EventRepository events;

    public CategoryService(CategoryRepository categories, EventRepository events) {
        this.categories = categories;
        this.events = events;
    }

    public CategoryResponse create(UUID eventId, CategoryRequest req) {
        Event event = events.findById(eventId)
            .orElseThrow(() -> new EntityNotFoundException("Event not found: " + eventId));
        if (categories.existsByEventIdAndName(eventId, req.name()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Category exists: " + req.name());
        int order = req.sortOrder() != null ? req.sortOrder() : categories.maxSortOrder(eventId) + 1;
        return CategoryResponse.from(categories.save(new Category(event, req.name(), order)));
    }

    public List<CategoryResponse> findAll(UUID eventId) {
        if (!events.existsById(eventId)) throw new EntityNotFoundException("Event not found: " + eventId);
        return categories.findByEventIdOrderBySortOrderAsc(eventId).stream()
            .map(CategoryResponse::from)
            .toList();
    }

    public CategoryResponse update(UUID eventId, UUID categoryId, CategoryRequest req) {
        Category category = categories.findByIdAndEventId(categoryId, eventId)
            .orElseThrow(() -> new EntityNotFoundException("Category not found: " + categoryId));
        if (categories.existsByEventIdAndNameAndIdNot(eventId, req.name(), categoryId))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Category exists: " + req.name());
        category.setName(req.name());
        if (req.sortOrder() != null) category.setSortOrder(req.sortOrder());
        return CategoryResponse.from(category); // managed entity, no save() needed
    }

    public void delete(UUID eventId, UUID categoryId) {
        Category category = categories.findByIdAndEventId(categoryId, eventId)
            .orElseThrow(() -> new EntityNotFoundException("Category not found: " + categoryId));
        if (!category.getProducts().isEmpty())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Category still has products");
        categories.delete(category);
    }

}
