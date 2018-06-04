package qa.tecnositafgulf.model.administration.inventory;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by ameljo on 6/3/18.
 */

@Entity
@Table(name = "transfers")
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

    @JoinTable(
            name = "item_transfer",
            joinColumns = {@JoinColumn(name = "transferId", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "itemId", referencedColumnName = "id")}
    )
    private List<Item> items;
}
