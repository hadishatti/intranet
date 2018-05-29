package qa.tecnositafgulf.model.inventory.stocks;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class ProductLocation implements Serializable{

    @Column(name = "product_Id")
    private Long productId;

    @Column(name = "location_Id")
    private Long locationId;

    public ProductLocation() {
    }

    public ProductLocation(Long productId, Long locationId) {
        this.productId = productId;
        this.locationId = locationId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }
}
