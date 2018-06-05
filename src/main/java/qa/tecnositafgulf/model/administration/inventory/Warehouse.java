package qa.tecnositafgulf.model.administration.inventory;

import qa.tecnositafgulf.model.companyInfo.Project;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by ameljo on 6/3/18.
 */

@Entity
@Table(name = "warehouse")
@NamedQueries({
        @NamedQuery(name = "Warehouse.listAllWarehouses", query = "SELECT warehouse From Warehouse warehouse"),
        @NamedQuery(name = "Warehouse.countAllWarehouses", query = "SELECT COUNT(warehouse) From Warehouse warehouse"),
        @NamedQuery(name = "Warehouse.getWarehouseByName", query = "SELECT warehouse From Warehouse warehouse WHERE warehouse.name = :name")
})
public class Warehouse implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String address;

    private Project project;

    @OneToOne(mappedBy = "warehouse")
    private InventoryCategory warehouseInventory;

    @OneToMany(mappedBy = "warehouse")
    List<Item> items;

    @OneToMany(mappedBy = "warehouse")
    List<InventoryCategory> categories;

    @OneToMany(mappedBy = "warehouse")
    List<InventoryLog> inventoryLogs;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public InventoryCategory getWarehouseInventory() {
        return warehouseInventory;
    }

    public void setWarehouseInventory(InventoryCategory warehouseInventory) {
        this.warehouseInventory = warehouseInventory;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public List<InventoryCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<InventoryCategory> categories) {
        this.categories = categories;
    }

    public List<InventoryLog> getInventoryLogs() {
        return inventoryLogs;
    }

    public void setInventoryLogs(List<InventoryLog> inventoryLogs) {
        this.inventoryLogs = inventoryLogs;
    }
}
