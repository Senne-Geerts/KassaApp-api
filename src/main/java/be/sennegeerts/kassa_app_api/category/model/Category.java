package be.sennegeerts.kassa_app_api.category.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import be.sennegeerts.kassa_app_api.event.model.Event;
import be.sennegeerts.kassa_app_api.product.model.Product;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "category",
       uniqueConstraints = @UniqueConstraint(columnNames = {"event_id", "name"}))
public class Category {
 
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
 
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sortOrder ASC")
    private List<Product> products = new ArrayList<>();
 
    @Column(nullable = false, length = 100)
    private String name;

 
    @Column(name = "sort_order", nullable = false)
    private int sortOrder;
 
    protected Category() {}
 
    public Category(Event event, String name, int sortOrder) {
        this.event = event;
        this.name = name;
        this.sortOrder = sortOrder;
    }
 
    public UUID getId() { return id; }
    public Event getEvent() { return event; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getSortOrder() { return sortOrder; }
    public void setSortOrder(int sortOrder) { this.sortOrder = sortOrder; }
}