package be.sennegeerts.kassa_app_api.event.repo;

import be.sennegeerts.kassa_app_api.event.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {
    Optional<Event> findBySlug(String slug);
    boolean existsBySlug(String slug);
    boolean existsBySlugAndIdNot(String slug, UUID id);
    List<Event> findByActiveTrueOrderByStartsAtDesc();
}
