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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(Date transferDate) {
        this.transferDate = transferDate;
    }

    public Warehouse getDestinationWarehouse() {
        return destinationWarehouse;
    }

    public void setDestinationWarehouse(Warehouse destinationWarehouse) {
        this.destinationWarehouse = destinationWarehouse;
    }

    public Warehouse getOriginWareHouse() {
        return originWareHouse;
    }

    public void setOriginWareHouse(Warehouse originWareHouse) {
        this.originWareHouse = originWareHouse;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
