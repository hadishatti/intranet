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
        @NamedQuery(name = "InventoryLog.getAllLogs",  query = "SELECT inventoryLog FROM InventoryLog inventoryLog")
})
public class InventoryLog implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Employee employee;

    private Date actionDate;

    private InventoryActionEnum action;

    private String description;

    @OneToMany
    private List<Item> items;
}
