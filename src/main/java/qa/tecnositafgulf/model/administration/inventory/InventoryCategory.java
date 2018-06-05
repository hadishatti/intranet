package qa.tecnositafgulf.model.administration.inventory;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by ameljo on 6/3/18.
 */

@Entity
@Table(name = "warehouse_inventory")
@NamedQueries({
        @NamedQuery(name = "InventoryCategory.getAllByWarehouse", query = "SELECT inventoryCategory FROM InventoryCategory inventoryCategory WHERE inventoryCategory.warehouse = :warehouse"),
        @NamedQuery(name = "InventoryCategory.countAllByWarehouse", query = "SELECT COUNT(inventoryCategory) FROM InventoryCategory inventoryCategory WHERE inventoryCategory.warehouse = :warehouse")
})
public class InventoryCategory implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String itemCategory;

    private String itemName;

    private String amount;

    private String unit;

    private int minQuantity;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(int minQuantity) {
        this.minQuantity = minQuantity;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }
}
