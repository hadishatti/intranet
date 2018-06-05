package qa.tecnositafgulf.model.administration.inventory;

import qa.tecnositafgulf.config.inventoryEnums.InventoryActionEnum;
import qa.tecnositafgulf.model.administration.Employee;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by ameljo on 6/3/18.
 */
@Entity
@Table(name = "inventory_log")
@NamedQueries({
        @NamedQuery(name = "InventoryLog.getAllLogs",  query = "SELECT inventoryLog FROM InventoryLog inventoryLog"),
        @NamedQuery(name = "InventoryLog.countAllLogs",  query = "SELECT COUNT(inventoryLog) FROM InventoryLog inventoryLog"),
        @NamedQuery(name = "InventoryLog.getAllLogsByEmployee",  query = "SELECT inventoryLog FROM InventoryLog inventoryLog WHERE inventoryLog.employee = :employee"),
        @NamedQuery(name = "InventoryLog.getAllLogsByActionDate",  query = "SELECT inventoryLog FROM InventoryLog inventoryLog WHERE inventoryLog.actionDate = :actionDate"),
        @NamedQuery(name = "InventoryLog.getAllLogsByAction",  query = "SELECT inventoryLog FROM InventoryLog inventoryLog WHERE inventoryLog.action = :action"),
        @NamedQuery(name = "InventoryLog.getAllLogsByWarehouse",  query = "SELECT inventoryLog FROM InventoryLog inventoryLog WHERE inventoryLog.warehouse = :warehouse")
})
public class InventoryLog implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date actionDate;

    private InventoryActionEnum action;

    private String description;

    @OneToMany
    private List<Item> items;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @ManyToOne
    @JoinColumn(name = "employeeID")
    private Employee employee;
}
