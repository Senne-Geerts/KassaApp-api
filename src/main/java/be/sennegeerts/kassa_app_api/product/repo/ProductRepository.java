package be.sennegeerts.kassa_app_api.product.repo;

import be.sennegeerts.kassa_app_api.product.model.Product;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findByCategoryIdOrderBySortOrderAsc(UUID categoryId);
    Optional<Product> findByIdAndCategoryId(UUID id, UUID categoryId);
    boolean existsByCategoryIdAndName(UUID categoryId, String name);
    boolean existsByCategoryIdAndNameAndIdNot(UUID categoryId, String name, UUID id);

    @Query("select coalesce(max(p.sortOrder), 0) from Product p where p.category.id = :categoryId")
    int maxSortOrder(UUID categoryId);
}