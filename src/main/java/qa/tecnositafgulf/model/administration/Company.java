package qa.tecnositafgulf.model.administration;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "company", uniqueConstraints = {@UniqueConstraint(columnNames={"name"})})
@NamedQueries({
        @NamedQuery(name = "Company.listAll", query = "SELECT company FROM Company company"),
        @NamedQuery(name = "Company.countAll", query = "SELECT count(company) FROM Company company")
})
public class Company implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String address;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString(){
        return name;
    }

    @Override
    public boolean equals(Object obj){
        if (obj == null) {
            return false;
        }
        if (!Company.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final Company other = (Company) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        return true;
    }
}
