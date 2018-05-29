package qa.tecnositafgulf.model.inventory.stocks;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity(name = "TransferStock")
@Table(name = "transferStock")
public class TransferStock implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fromLocationName")
    private String fromLocationName;

    @Column(name = "toLocationName")
    private String toLocationName;

    @Column(name = "productName")
    private String productName;

    @Column(name = "noOfStocksTransfer")
    private Long noOfStocksTransfer;

    @Column(name = "createDate")
    private Date createDate;

    public String getFromLocationName() {
        return fromLocationName;
    }

    public void setFromLocationName(String fromLocationName) {
        this.fromLocationName = fromLocationName;
    }

    public String getToLocationName() {
        return toLocationName;
    }

    public void setToLocationName(String toLocationName) {
        this.toLocationName = toLocationName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getNoOfStocksTransfer() {
        return noOfStocksTransfer;
    }

    public void setNoOfStocksTransfer(Long noOfStocksTransfer) {
        this.noOfStocksTransfer = noOfStocksTransfer;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransferStock that = (TransferStock) o;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getFromLocationName(), that.getFromLocationName()) &&
                Objects.equals(getToLocationName(), that.getToLocationName()) &&
                Objects.equals(getProductName(), that.getProductName());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getFromLocationName(), getToLocationName(), getProductName());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
