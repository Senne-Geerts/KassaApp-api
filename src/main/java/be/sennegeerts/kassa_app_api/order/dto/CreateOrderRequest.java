package be.sennegeerts.kassa_app_api.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public record CreateOrderRequest(
        @NotNull UUID clientId,
        @NotEmpty @Valid List<OrderLineRequest> lines
) {}