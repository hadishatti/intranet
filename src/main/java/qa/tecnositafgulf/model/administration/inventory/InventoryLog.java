package qa.tecnositafgulf.model.administration.inventory;

import qa.tecnositafgulf.config.inventoryEnums.InventoryActionEnum;
import qa.tecnositafgulf.model.administration.Employee;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by ameljo on 6/3/18.
 */
public class InventoryLog implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Employee employee;

    private Date actionDate;

    private InventoryActionEnum action;

    private String description;
}
