package be.sennegeerts.kassa_app_api.order.dto;

import be.sennegeerts.kassa_app_api.order.model.Order;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record OrderResponse(UUID id, UUID clientId, BigDecimal total,
                            Instant createdAt, List<OrderLineResponse> lines) {
    public static OrderResponse from(Order o) {
        return new OrderResponse(o.getId(), o.getClientId(), o.getTotal(), o.getCreatedAt(),
                o.getLines().stream().map(OrderLineResponse::from).toList());
    }
}