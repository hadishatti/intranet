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

    private String name;

    private String address;

    private Project project;

    @OneToOne(mappedBy = "warehouse")
    private InventoryCategory warehouseInventory;

    @OneToMany(mappedBy = "warehouse")
    List<Item> items;

    @OneToMany(mappedBy = "warehouse")
    List<InventoryCategory> categories;
}
