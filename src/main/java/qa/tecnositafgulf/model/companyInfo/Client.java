package qa.tecnositafgulf.model.companyInfo;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by klajdi on 05/05/18.
 */

@Entity
@Table(name = "client", uniqueConstraints = {@UniqueConstraint(columnNames={"name"})})
@NamedQueries({
        @NamedQuery(name = "Client.listAll", query = "SELECT client FROM Client client"),
        @NamedQuery(name = "Client.countAll", query = "SELECT count(client) FROM Client client")
})
public class Client implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private String img;

    private String link;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getImg() {
        return img;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }
}
