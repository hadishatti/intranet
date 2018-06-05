package qa.tecnositafgulf.model.administration.inventory;

import qa.tecnositafgulf.model.administration.Employee;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by ameljo on 6/3/18.
 */

@Entity
@Table(name = "transfer")
@NamedQueries({
        @NamedQuery(name = "Transfer.getAllTransfers", query = "SELECT transfer FROM Transfer transfer"),
        @NamedQuery(name = "Transfer.countAllTransfers", query = "SELECT COUNT(transfer) FROM Transfer transfer")
})
public class Transfer implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date transferDate;

    private Warehouse originWareHouse;

    private Warehouse destinationWarehouse;

    private int amount;

    private String unit;

    private String itemName;

    private String itemCategory;

    @ManyToMany
    @JoinTable(
            name = "item_transfer",
            joinColumns = {@JoinColumn(name = "transferID", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "itemID", referencedColumnName = "id")}
    )
    private List<Item> items;

    @ManyToOne
    @JoinColumn(name = "employeeID")
    private Employee employee;
}
