package be.sennegeerts.kassa_app_api.order.controller;

import be.sennegeerts.kassa_app_api.order.dto.*;
import be.sennegeerts.kassa_app_api.order.service.OrderService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events/{eventId}/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) { this.orderService = orderService; }

    @PostMapping
    public ResponseEntity<OrderResponse> create(@PathVariable UUID eventId,
                                                @Valid @RequestBody CreateOrderRequest req) {
        OrderResponse created = orderService.create(eventId, req);
        return ResponseEntity.created(URI.create(
                "/api/events/" + eventId + "/orders/" + created.id())).body(created);
    }

    @GetMapping
    public List<OrderResponse> getAll(@PathVariable UUID eventId) {
        return orderService.findAll(eventId);
    }
}