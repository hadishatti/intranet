package qa.tecnositafgulf.model.administration.inventory;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by ameljo on 6/3/18.
 */

@Entity
@Table(name = "warehouse_inventory")
public class InventoryCategory implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String itemCategory;

    private String itemName;

    private String amount;

    private String unit;

    private int minQuantity;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;
}
