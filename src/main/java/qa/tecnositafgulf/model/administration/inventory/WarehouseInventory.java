package qa.tecnositafgulf.model.administration.inventory;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by ameljo on 6/3/18.
 */

@Entity
@Table(name = "warehouse_inventory")
public class WarehouseInventory implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String itemCategory;

    private String itemName;

    private String amount;

    private String unit;

    private int inUse;

    private int defective;

    private int inStorage;

    private int minQuantity;

    @ManyToOne
    private Warehouse warehouse;
}
