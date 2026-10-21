package be.sennegeerts.kassa_app_api.category.controller;

import be.sennegeerts.kassa_app_api.category.dto.CategoryRequest;
import be.sennegeerts.kassa_app_api.category.dto.CategoryResponse;
import be.sennegeerts.kassa_app_api.category.service.CategoryService;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events/{eventId}/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<CategoryResponse> getAllCategories(@PathVariable UUID eventId) {
        return categoryService.findAll(eventId);
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@PathVariable UUID eventId,
                                                           @Valid @RequestBody CategoryRequest req) {
        CategoryResponse created = categoryService.create(eventId, req);
        return ResponseEntity
                .created(URI.create("/api/events/" + eventId + "/categories/" + created.id()))
                .body(created);
    }

    @PutMapping("/{categoryId}")
    public CategoryResponse updateCategory(@PathVariable UUID eventId,
                                           @PathVariable UUID categoryId,
                                           @Valid @RequestBody CategoryRequest req) {
        return categoryService.update(eventId, categoryId, req);
    }

    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable UUID eventId, @PathVariable UUID categoryId) {
        categoryService.delete(eventId, categoryId);
    }
}