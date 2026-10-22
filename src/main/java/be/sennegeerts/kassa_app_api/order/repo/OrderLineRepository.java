package be.sennegeerts.kassa_app_api.order.repo;

import be.sennegeerts.kassa_app_api.order.model.OrderLine;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderLineRepository extends JpaRepository<OrderLine, UUID> {
    boolean existsByProductId(UUID productId);
    boolean existsByProductCategoryId(UUID categoryId);
}