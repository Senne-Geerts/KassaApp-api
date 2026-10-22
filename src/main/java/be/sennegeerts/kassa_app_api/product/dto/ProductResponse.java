package be.sennegeerts.kassa_app_api.product.dto;

import be.sennegeerts.kassa_app_api.product.model.Product;
import java.math.BigDecimal;
import java.util.UUID;

public record ProductResponse(UUID id, String name, BigDecimal price, int sortOrder) {
    public static ProductResponse from(Product p) {
        return new ProductResponse(p.getId(), p.getName(), p.getPrice(), p.getSortOrder());
    }
}