package be.sennegeerts.kassa_app_api.category.model;

import java.util.UUID;

import be.sennegeerts.kassa_app_api.event.model.Event;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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