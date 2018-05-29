package qa.tecnositafgulf.model.inventory.location;

import qa.tecnositafgulf.model.administration.Company;
import qa.tecnositafgulf.model.inventory.product.Product;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity(name = "Location")
@Table(name = "location", uniqueConstraints = {@UniqueConstraint(columnNames={"name"})})
@NamedQueries({
        @NamedQuery(name = "Location.getAllLocations", query = "select locations from Location locations order by id desc"),
        @NamedQuery(name = "Location.getLocationByName", query = "select location from Location location where location.name=:name")
})
public class Location implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "address")
    private String address;

    @Column(name = "createDate")
    private Date createDate;

    @ManyToOne
    @JoinColumn(name = "company_Id")
    private Company company;

    @ManyToMany(mappedBy = "locationList", fetch = FetchType.EAGER)
    private List<Product> productList;


    public Location() {
    }

    public Location(Long id, String name, String description, String address, Date createDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.address = address;
        this.createDate = createDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Objects.equals(getName(), location.getName()) &&
                Objects.equals(getDescription(), location.getDescription());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getName(), getDescription());
    }

    @Override
    public String toString() {
        return name;

    }
}
