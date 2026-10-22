package be.sennegeerts.kassa_app_api.product.controller;

import be.sennegeerts.kassa_app_api.product.dto.*;
import be.sennegeerts.kassa_app_api.product.service.ProductService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events/{eventId}/categories/{categoryId}/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) { this.productService = productService; }

    @GetMapping
    public List<ProductResponse> getAll(@PathVariable UUID eventId, @PathVariable UUID categoryId) {
        return productService.findAll(eventId, categoryId);
    }

    @PostMapping
    public ResponseEntity<ProductResponse> create(@PathVariable UUID eventId, @PathVariable UUID categoryId,
                                                  @Valid @RequestBody ProductRequest req) {
        ProductResponse created = productService.create(eventId, categoryId, req);
        return ResponseEntity.created(URI.create(
                "/api/events/" + eventId + "/categories/" + categoryId + "/products/" + created.id()))
                .body(created);
    }

    @PutMapping("/{productId}")
    public ProductResponse update(@PathVariable UUID eventId, @PathVariable UUID categoryId,
                                  @PathVariable UUID productId, @Valid @RequestBody ProductRequest req) {
        return productService.update(eventId, categoryId, productId, req);
    }

    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID eventId, @PathVariable UUID categoryId, @PathVariable UUID productId) {
        productService.delete(eventId, categoryId, productId);
    }
}