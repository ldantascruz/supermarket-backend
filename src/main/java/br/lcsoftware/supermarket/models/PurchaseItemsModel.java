package br.lcsoftware.supermarket.models;

import jakarta.persistence.*;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name= "purchase_items")
public class PurchaseItemsModel extends RepresentationModel<PurchaseItemsModel> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name= "id", unique = true, insertable = false, updatable = false, nullable = false)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "purchase_id", referencedColumnName = "id")
    private PurchasesModel purchase;
    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private ItemModel item;
    @Column(name="quantity", nullable = false)
    private Integer quantity;
    @Column(name="unit_price", nullable = false)
    private Float unitPrice;
    @Column(name = "total_price", nullable = false)
    private Float totalPrice;
    @Column(name = "created_at", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;
    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp updatedAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public PurchasesModel getPurchase() {
        return purchase;
    }

    public void setPurchase(PurchasesModel purchase) {
        this.purchase = purchase;
    }

    public ItemModel getItem() {
        return item;
    }

    public void setItem(ItemModel item) {
        this.item = item;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Float totalPrice) {
        this.totalPrice = totalPrice;
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

    @PrePersist
    public void prePersist() {
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.updatedAt = new Timestamp(System.currentTimeMillis());
        this.totalPrice = this.unitPrice * this.quantity;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = new Timestamp(System.currentTimeMillis());
        this.totalPrice = this.unitPrice * this.quantity;
    }
}
