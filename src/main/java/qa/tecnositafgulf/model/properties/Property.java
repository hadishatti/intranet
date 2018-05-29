package qa.tecnositafgulf.model.properties;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by ameljo on 5/15/18.
 */

@Entity
@Table(name = "properties", uniqueConstraints = {@UniqueConstraint(columnNames = {"`key`"})})
@NamedQueries({
        @NamedQuery(name = "Property.listAll", query = "SELECT property FROM Property property")
})
public class Property implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "`key`")
    private String key;

    private String description;

    private String value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
