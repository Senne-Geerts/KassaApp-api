package be.sennegeerts.kassa_app_api.order.repo;

import be.sennegeerts.kassa_app_api.order.model.Order;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    Optional<Order> findByClientId(UUID clientId);
    List<Order> findByEventIdOrderByCreatedAtDesc(UUID eventId);
}