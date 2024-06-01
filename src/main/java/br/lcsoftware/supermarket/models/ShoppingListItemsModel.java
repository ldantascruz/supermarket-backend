package br.lcsoftware.supermarket.models;

import jakarta.persistence.*;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "shopping_list_items")
public class ShoppingListItemsModel extends RepresentationModel<ShoppingListItemsModel> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, insertable = false, updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "shopping_list_id", referencedColumnName = "id")
    private ShoppingListsModel shoppingList;

    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private ItemModel item;

    @Column(name = "created_at", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp updatedAt;

    @Transient
    private Double totalPrice;

    // Getters and Setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ShoppingListsModel getShoppingList() {
        return shoppingList;
    }

    public void setShoppingList(ShoppingListsModel shoppingList) {
        this.shoppingList = shoppingList;
    }

    public ItemModel getItem() {
        return item;
    }

    public void setItem(ItemModel item) {
        this.item = item;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Float getTotalPrice() {
        if (this.item != null) {
            return this.item.getUnitPrice() * this.item.getQuantity();
        }
        return 0.0f;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.updatedAt = new Timestamp(System.currentTimeMillis());
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Timestamp(System.currentTimeMillis());
    }
}
