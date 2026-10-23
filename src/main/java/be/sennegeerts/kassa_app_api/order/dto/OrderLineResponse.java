package be.sennegeerts.kassa_app_api.order.dto;

import be.sennegeerts.kassa_app_api.order.model.OrderLine;
import java.math.BigDecimal;
import java.util.UUID;

public record OrderLineResponse(UUID productId, String name, int quantity,
                                BigDecimal unitPrice, BigDecimal lineTotal) {
    public static OrderLineResponse from(OrderLine l) {
        return new OrderLineResponse(
                l.getProduct().getId(),
                l.getNameSnapshot(),
                l.getQuantity(),
                l.getUnitPrice(),
                l.getUnitPrice().multiply(BigDecimal.valueOf(l.getQuantity())));
    }
}