package qa.tecnositafgulf.model.inventory.stocks;

import qa.tecnositafgulf.model.inventory.location.Location;
import qa.tecnositafgulf.model.inventory.product.Product;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "ProductStock")
@Table(name = "productStock")
@NamedQueries({@NamedQuery(name = "ProductStock.getAllProductStock", query = "select productStock from ProductStock productStock order by id desc")})
public class ProductStock implements Serializable{

    @EmbeddedId
    private ProductLocation productLocation;

    @Column(name = "thresholdLimit")
    private Long thresholdLimit;

    @Column(name = "currentStock")
    private Long currentStock;

    @Column(name = "unitPrice")
    private Long unitPrice;

    @Column(name = "createDate")
    private Date createDate;

    @ManyToOne
    @MapsId(value = "product_Id")
    private Product product;

    @ManyToOne
    @MapsId(value = "location_Id")
    private Location location;


    public ProductStock() {
    }

    public ProductStock(Long thresholdLimit, Long currentStock, Long unitPrice, Date createDate) {
        this.thresholdLimit = thresholdLimit;
        this.currentStock = currentStock;
        this.unitPrice = unitPrice;
        this.createDate = createDate;

    }

    public ProductLocation getProductLocation() {
        return productLocation;
    }

    public void setProductLocation(ProductLocation productLocation) {
        this.productLocation = productLocation;
    }

    public Long getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Long unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Long getThresholdLimit() {
        return thresholdLimit;
    }

    public void setThresholdLimit(Long thresholdLimit) {
        this.thresholdLimit = thresholdLimit;
    }

    public Long getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(Long currentStock) {
        this.currentStock = currentStock;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }


    @Override
    public String toString() {
        return "ProductStock{" +
                "thresholdLimit=" + thresholdLimit +
                ", currentStock=" + currentStock +
                ", unitPrice=" + unitPrice +
                ", createDate=" + createDate +
                '}';
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
