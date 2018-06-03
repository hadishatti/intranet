package qa.tecnositafgulf.model.administration.inventory;

import qa.tecnositafgulf.model.administration.Employee;
import qa.tecnositafgulf.model.suppliers.Supplier;

import javax.annotation.Generated;
import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by ameljo on 6/3/18.
 */

@Entity
@Table(name = "items")
public class Item implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String serialNr;

    private String Category;

    private String name;

    private String brand;

    private String unit;

    private String status;

    private String desc;

    private Employee employee;

    private double price;

    @ManyToOne
    private Warehouse warehouse;

    @ManyToOne
    private Supplier supplier;

}
