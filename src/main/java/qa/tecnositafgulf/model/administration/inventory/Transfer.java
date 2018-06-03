package qa.tecnositafgulf.model.administration.inventory;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

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
}
