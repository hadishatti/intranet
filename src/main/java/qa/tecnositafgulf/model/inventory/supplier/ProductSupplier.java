package qa.tecnositafgulf.model.inventory.supplier;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "ProductSupplier")
@Table(name = "productSupplier")
public class ProductSupplier implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String address;

    private String mobileNumber;

    private String landLineNumber;

    private String emailId;

    private Date createDate;

    public ProductSupplier() {
    }

    public ProductSupplier(Long id, String name, String address, String mobileNumber, String landLineNumber, String emailId, Date createDate) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.mobileNumber = mobileNumber;
        this.landLineNumber = landLineNumber;
        this.emailId = emailId;
        this.createDate = createDate;
    }

    public Long getId() {
        return id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getLandLineNumber() {
        return landLineNumber;
    }

    public void setLandLineNumber(String landLineNumber) {
        this.landLineNumber = landLineNumber;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

}
