package be.sennegeerts.kassa_app_api.order.model;

import be.sennegeerts.kassa_app_api.product.model.Product;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;
 
@Entity
@Table(name = "order_line",
       uniqueConstraints = @UniqueConstraint(columnNames = {"order_id", "product_id"}))
public class OrderLine {
 
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
 
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
 
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
 
    @Column(nullable = false)
    private int quantity;
 
    /** Price and name at the moment of sale, so later edits never rewrite history. */
    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;
 
    @Column(name = "name_snapshot", nullable = false, length = 100)
    private String nameSnapshot;
 
    protected OrderLine() {}
 
    public OrderLine(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = product.getPrice();
        this.nameSnapshot = product.getName();
    }
 
    void setOrder(Order order) { this.order = order; }
 
    public UUID getId() { return id; }
    public Order getOrder() { return order; }
    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public String getNameSnapshot() { return nameSnapshot; }
}
