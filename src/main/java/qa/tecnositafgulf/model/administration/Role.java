package qa.tecnositafgulf.model.administration;

import javax.persistence.*;

@Entity
@Table(name = "role",  uniqueConstraints = {@UniqueConstraint(columnNames={"name"})})
@NamedQueries({
        @NamedQuery(name = "Role.listAll", query = "SELECT role FROM Role role"),
        @NamedQuery(name = "Role.countAll", query = "SELECT count(role) FROM Role role")
})
public class Role {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String description;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString(){
        return description;
    }

    @Override
    public boolean equals(Object obj){
        if (obj == null) {
            return false;
        }
        if (!Role.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final Role other = (Role) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }
}
