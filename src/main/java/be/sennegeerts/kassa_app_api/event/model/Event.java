package be.sennegeerts.kassa_app_api.event.model;

import be.sennegeerts.kassa_app_api.category.model.Category;
import be.sennegeerts.kassa_app_api.order.model.Order;
import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    // Used for the URL, e.g. /pasta or /kaaswijn (replaces the hardcoded routes)
    @Column(nullable = false, unique = true)
    private String slug;

    private LocalDate startsAt;

    // Only active events show up on the home screen
    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sortOrder ASC")
    private List<Category> categories = new ArrayList<>();

    @OneToMany(mappedBy = "event")
    private List<Order> orders = new ArrayList<>();

    protected Event() {
        // required by JPA
    }

    public Event(String name, String slug, LocalDate startsAt) {
        this.name = name;
        this.slug = slug;
        this.startsAt = startsAt;
    }

    // --- domain behaviour ---

/*     public void addCategory(Category category) {
        categories.add(category);
        category.setEvent(this);
    }

    /**
     * Copies this event with its categories and products, so a new edition
     * (e.g. next year's Pasta di Skippy) starts with last year's menu.
     * Orders are NOT copied.
     
    public Event cloneAs(String newName, String newSlug, LocalDate newStartsAt) {
        Event copy = new Event(newName, newSlug, newStartsAt);
        for (Category c : categories) {
            copy.addCategory(c.copy());
        }
        return copy;
    } */

    // --- getters / setters ---

    public UUID getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public LocalDate getStartsAt() { return startsAt; }
    public void setStartsAt(LocalDate startsAt) { this.startsAt = startsAt; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public Instant getCreatedAt() { return createdAt; }

    public List<Category> getCategories() { return categories; }
    public List<Order> getOrders() { return orders; }
}