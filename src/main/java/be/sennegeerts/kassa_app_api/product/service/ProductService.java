package be.sennegeerts.kassa_app_api.product.service;

import be.sennegeerts.kassa_app_api.category.model.Category;
import be.sennegeerts.kassa_app_api.category.repo.CategoryRepository;
import be.sennegeerts.kassa_app_api.order.repo.OrderLineRepository;
import be.sennegeerts.kassa_app_api.product.dto.*;
import be.sennegeerts.kassa_app_api.product.model.Product;
import be.sennegeerts.kassa_app_api.product.repo.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class ProductService {
    private final ProductRepository products;
    private final CategoryRepository categories;
    private final OrderLineRepository orderLines;

    public ProductService(ProductRepository products, CategoryRepository categories,
                          OrderLineRepository orderLines) {
        this.products = products;
        this.categories = categories;
        this.orderLines = orderLines;
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> findAll(UUID eventId, UUID categoryId) {
        requireCategory(eventId, categoryId);
        return products.findByCategoryIdOrderBySortOrderAsc(categoryId).stream()
                .map(ProductResponse::from).toList();
    }

    public ProductResponse create(UUID eventId, UUID categoryId, ProductRequest req) {
        Category category = requireCategory(eventId, categoryId);
        if (products.existsByCategoryIdAndName(categoryId, req.name()))
            throw nameTaken(req.name());
        int order = req.sortOrder() != null ? req.sortOrder() : products.maxSortOrder(categoryId) + 1;
        return ProductResponse.from(products.save(new Product(category, req.name(), req.price(), order)));
    }

    public ProductResponse update(UUID eventId, UUID categoryId, UUID productId, ProductRequest req) {
        requireCategory(eventId, categoryId);
        Product product = requireProduct(categoryId, productId);
        if (products.existsByCategoryIdAndNameAndIdNot(categoryId, req.name(), productId))
            throw nameTaken(req.name());
        product.setName(req.name());
        product.setPrice(req.price());   // old orders keep their own unit_price snapshot
        if (req.sortOrder() != null) product.setSortOrder(req.sortOrder());
        return ProductResponse.from(product);
    }

    public void delete(UUID eventId, UUID categoryId, UUID productId) {
        Category category = requireCategory(eventId, categoryId);
        Product product = requireProduct(categoryId, productId);
        if (orderLines.existsByProductId(productId))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Product already sold, cannot delete");
        category.getProducts().remove(product);   // orphanRemoval deletes it
    }

    private Category requireCategory(UUID eventId, UUID categoryId) {
        return categories.findByIdAndEventId(categoryId, eventId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found: " + categoryId));
    }

    private Product requireProduct(UUID categoryId, UUID productId) {
        return products.findByIdAndCategoryId(productId, categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found: " + productId));
    }

    private ResponseStatusException nameTaken(String name) {
        return new ResponseStatusException(HttpStatus.CONFLICT, "Product exists in category: " + name);
    }
}