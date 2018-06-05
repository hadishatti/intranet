package qa.tecnositafgulf.model.administration.inventory;

import qa.tecnositafgulf.config.inventoryEnums.ItemStatusEnum;
import qa.tecnositafgulf.model.administration.Employee;
import qa.tecnositafgulf.model.suppliers.Supplier;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by ameljo on 6/3/18.
 */

@Entity
@Table(name = "item")
@NamedQueries({
        @NamedQuery(name = "Item.listAllItems", query = "SELECT item FROM Item item"),
        @NamedQuery(name = "Item.countAllItems", query = "SELECT COUNT(item) FROM Item item"),
        @NamedQuery(name = "Item.getItemsByStatus", query = "SELECT item FROM Item item WHERE item.status = :status"),
        @NamedQuery(name = "Item.countItemsByStatus", query = "SELECT COUNT(item) FROM Item item WHERE item.status = :status"),
        @NamedQuery(name = "Item.getItemsByName", query = "SELECT item FROM Item item WHERE item.name = :name"),
        @NamedQuery(name = "Item.countItemsByName", query = "SELECT COUNT(item) FROM Item item WHERE item.name = :name"),
        @NamedQuery(name = "Item.getItemsByCategory", query = "SELECT item FROM Item item WHERE item.category = :category"),
        @NamedQuery(name = "Item.countItemsByCategory", query = "SELECT COUNT(item) FROM Item item WHERE item.category = :category"),
        @NamedQuery(name = "Item.getItemsByBrand", query = "SELECT item FROM Item item WHERE item.brand = :brand"),
        @NamedQuery(name = "Item.countItemsByBrand", query = "SELECT COUNT(item) FROM Item item WHERE item.brand = :brand")
})
public class Item implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String serialNumber;

    private String category;

    private String name;

    private String brand;

    private String unit;

    private ItemStatusEnum status;

    @Lob
    private String description;

    private double price;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @ManyToOne
    private Supplier supplier;

    @ManyToMany(mappedBy = "items")
    private List<Transfer> transfers;

    @ManyToOne
    @JoinColumn(name = "employeeID")
    private Employee employee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public ItemStatusEnum getStatus() {
        return status;
    }

    public void setStatus(ItemStatusEnum status) {
        this.status = status;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public List<Transfer> getTransfers() {
        return transfers;
    }

    public void setTransfers(List<Transfer> transfers) {
        this.transfers = transfers;
    }
}
