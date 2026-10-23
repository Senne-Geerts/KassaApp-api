package be.sennegeerts.kassa_app_api.order.service;

import be.sennegeerts.kassa_app_api.event.model.Event;
import be.sennegeerts.kassa_app_api.event.repo.EventRepository;
import be.sennegeerts.kassa_app_api.order.dto.*;
import be.sennegeerts.kassa_app_api.order.model.Order;
import be.sennegeerts.kassa_app_api.order.model.OrderLine;
import be.sennegeerts.kassa_app_api.order.repo.OrderRepository;
import be.sennegeerts.kassa_app_api.product.model.Product;
import be.sennegeerts.kassa_app_api.product.repo.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class OrderService {
    private final OrderRepository orders;
    private final EventRepository events;
    private final ProductRepository products;

    public OrderService(OrderRepository orders, EventRepository events, ProductRepository products) {
        this.orders = orders;
        this.events = events;
        this.products = products;
    }

    public OrderResponse create(UUID eventId, CreateOrderRequest req) {
        // Retry of the same sale: return what was already booked
        Optional<Order> existing = orders.findByClientId(req.clientId());
        if (existing.isPresent()) {
            if (!existing.get().getEvent().getId().equals(eventId))
                throw new ResponseStatusException(HttpStatus.CONFLICT, "clientId already used");
            return OrderResponse.from(existing.get());
        }

        Event event = events.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found: " + eventId));
        if (!event.isActive())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Event is not active");

        // Same product twice in the request -> one line, quantities added
        Map<UUID, Integer> quantities = new LinkedHashMap<>();
        for (OrderLineRequest line : req.lines())
            quantities.merge(line.productId(), line.quantity(), Integer::sum);

        Map<UUID, Product> found = products
                .findByIdInAndCategoryEventId(quantities.keySet(), eventId).stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));
        if (found.size() != quantities.size())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown product for this event");

        Order order = new Order(event, req.clientId());
        quantities.forEach((productId, qty) ->
                order.addLine(new OrderLine(found.get(productId), qty)));

        return OrderResponse.from(orders.save(order));
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> findAll(UUID eventId) {
        if (!events.existsById(eventId))
            throw new EntityNotFoundException("Event not found: " + eventId);
        return orders.findByEventIdOrderByCreatedAtDesc(eventId).stream()
                .map(OrderResponse::from).toList();
    }
}