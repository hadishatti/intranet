package qa.tecnositafgulf.model.inventory.productCategory;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity(name = "ProductCategory")
@Table(name = "productCategory", uniqueConstraints = {@UniqueConstraint(columnNames={"productCatgName"})})
@NamedQueries(
        @NamedQuery(name = "ProductCategory.getAllProductCategory", query = "select productCategory from ProductCategory productCategory order by id")
)
public class ProductCategory implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;

    private String productCatgName;

    private String productCatgDesc;

    private Date createdDate;

/*    @OneToMany(mappedBy = "productCategory", orphanRemoval = true, fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    private List<Product> productList;*/

    public ProductCategory() {
    }

    public ProductCategory(String productCatgName, String productCatgDesc) {
        this.productCatgName = productCatgName;
        this.productCatgDesc = productCatgDesc;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductCatgName() {
        return productCatgName;
    }

    public void setProductCatgName(String productCatgName) {
        this.productCatgName = productCatgName;
    }

    public String getProductCatgDesc() {
        return productCatgDesc;
    }

    public void setProductCatgDesc(String productCatgDesc) {
        this.productCatgDesc = productCatgDesc;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

/*    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }*/


    @Override
    public String toString() {
        return productCatgName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductCategory that = (ProductCategory) o;
        return Objects.equals(getProductCatgName(), that.getProductCatgName()) &&
                Objects.equals(getProductCatgDesc(), that.getProductCatgDesc());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getProductCatgName(), getProductCatgDesc());
    }
}
