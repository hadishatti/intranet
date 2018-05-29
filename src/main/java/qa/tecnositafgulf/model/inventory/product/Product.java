package qa.tecnositafgulf.model.inventory.product;

import qa.tecnositafgulf.model.inventory.location.Location;
import qa.tecnositafgulf.model.inventory.productCategory.ProductCategory;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity(name = "Product")
@Table(name = "product", uniqueConstraints = {@UniqueConstraint(columnNames={"productName"})})
@NamedQueries({
        @NamedQuery(name = "Product.getAllProducts", query = "select products from Product products order by id")

})
public class Product implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String productName;
    private String productDesc;
    private Date createDate;

    @ManyToOne
    @JoinColumn(name = "productCatg_Id")
    private ProductCategory productCategory;



    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(name = "product_location",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "location_id")
    )
    private List<Location> locationList;

    public Product() {

    }

    public Product(String productName, String productDesc, Date createDate, ProductCategory productCategory) {
        this.productName = productName;
        this.productDesc = productDesc;
        this.createDate = createDate;
        this.productCategory = productCategory;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

   /* public ProductStock getProductStock() {
        return productStock;
    }

    public void setProductStock(ProductStock productStock) {
        this.productStock = productStock;
    }
*/
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(getProductName(), product.getProductName()) &&
                Objects.equals(getProductDesc(), product.getProductDesc());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProductName(), getProductDesc());
    }

    @Override
    public String toString() {
        return productName;
    }


   public List<Location> getLocationList() {
        return locationList;
    }

    public void setLocationList(List<Location> locationList) {
        this.locationList = locationList;
    }
}
