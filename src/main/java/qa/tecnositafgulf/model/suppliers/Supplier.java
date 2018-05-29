package qa.tecnositafgulf.model.suppliers;

import javax.persistence.*;

/**
 * Created by ledio on 5/5/18.
 */
@Entity(name = "Supplier")
@Table(name = "supplier")
@NamedQueries({
        @NamedQuery(name = "Supplier.listAll", query = "SELECT supplier FROM Supplier supplier"),
        @NamedQuery(name = "Supplier.findByName", query = "SELECT supplier FROM Supplier supplier WHERE supplier.name = :name")
})
public class Supplier {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String domainURL;
    private String imgSrc;

    public Supplier(String name, String domainURL, String imgSrc) {
        this.name = name;
        this.domainURL = domainURL;
        this.imgSrc = imgSrc;
    }

    public Supplier() {
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

    public String getDomainURL() {
        return domainURL;
    }

    public void setDomainURL(String domainURL) {
        this.domainURL = domainURL;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }
}
