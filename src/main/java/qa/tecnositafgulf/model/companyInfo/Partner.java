package qa.tecnositafgulf.model.companyInfo;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by ameljo on 05/05/18.
 */

@Entity
@Table(name = "partner")
@NamedQueries({
        @NamedQuery(name = "Partner.listAll", query = "SELECT partner FROM Partner partner"),
        @NamedQuery(name = "Partner.countAll", query = "SELECT count(partner) FROM Partner partner")
})
public class Partner implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String href;

    private String img;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
