package qa.tecnositafgulf.model.administration.inventory;

import qa.tecnositafgulf.model.companyInfo.Project;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by ameljo on 6/3/18.
 */

@Entity
@Table(name = "warehouses")
public class Warehouse implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String address;

    private Project project;
}
