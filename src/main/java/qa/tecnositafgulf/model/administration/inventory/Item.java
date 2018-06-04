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
@Table(name = "items")
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

    private Employee employee;

    private double price;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @ManyToOne
    private Supplier supplier;

    @ManyToMany(mappedBy = "items")
    private List<Transfer> transfers;

}
