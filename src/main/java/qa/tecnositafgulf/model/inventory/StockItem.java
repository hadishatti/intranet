package qa.tecnositafgulf.model.inventory;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by hadi on 1/28/18.
 */
public class StockItem implements Serializable{

    private Long id;

    private String number;

    private String serialNumber;

    private String name;

    private String description;

    private Vendor vendor;


    private StockLocation location;

    private double costPerItem;

    private String reorderPoint; //minimum amount of an item which the company holds in stock, such that, when stock falls to this amount, the item must be reordered

    private int daysForReorder;

    private boolean warranty;

    private Date warrantyExpiration;






}
