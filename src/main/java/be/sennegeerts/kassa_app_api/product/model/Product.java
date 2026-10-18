package be.sennegeerts.kassa_app_api.product.model;

import be.sennegeerts.kassa_app_api.category.model.Category;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;
 
@Entity
@Table(name = "product")
public class Product {
 
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
 
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
 
    @Column(nullable = false, length = 100)
    private String name;
 
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
 
    @Column(name = "sort_order", nullable = false)
    private int sortOrder;
 
    protected Product() {}
 
    public Product(Category category, String name, BigDecimal price, int sortOrder) {
        this.category = category;
        this.name = name;
        this.price = price;
        this.sortOrder = sortOrder;
    }

    /** Copy of this product that belongs to newCategory. */
    public Product copyFor(Category newCategory) {
        return new Product(newCategory, name, price, sortOrder);
    }
 
    public UUID getId() { return id; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public int getSortOrder() { return sortOrder; }
    public void setSortOrder(int sortOrder) { this.sortOrder = sortOrder; }
}