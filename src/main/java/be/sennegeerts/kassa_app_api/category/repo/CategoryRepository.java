package be.sennegeerts.kassa_app_api.category.repo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import be.sennegeerts.kassa_app_api.category.model.Category;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    List<Category> findByEventIdOrderBySortOrderAsc(UUID eventId);
    boolean existsByEventIdAndName(UUID eventId, String name);
    boolean existsByEventIdAndNameAndIdNot(UUID eventId, String name, UUID id);
    Optional<Category> findByIdAndEventId(UUID id, UUID eventId);

    @Query("select coalesce(max(c.sortOrder), 0) from Category c where c.event.id = :eventId")
    int maxSortOrder(UUID eventId);
}