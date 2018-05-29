package qa.tecnositafgulf.model.administration;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by hadi on 12/17/17.
 */

@Entity
@Table(name = "profile", uniqueConstraints = {@UniqueConstraint(columnNames={"name"})})
@NamedQueries({
        @NamedQuery(name = "Profile.listAll", query = "SELECT profile FROM Profile profile"),
        @NamedQuery(name = "Profile.countAll", query = "SELECT count(profile) FROM Profile profile"),
        @NamedQuery(name = "Profile.getDefaultProfile", query = "SELECT profile FROM Profile profile WHERE defaultProfile=true"),
        @NamedQuery(name = "Profile.findByName", query = "SELECT profile FROM Profile profile WHERE profile.name = :name")
})
public class Profile implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private boolean defaultProfile;

    public Profile(){ }

    public Profile(String name, String description) {
        this.name = name;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDefaultProfile() {
        return defaultProfile;
    }

    public void setDefaultProfile(boolean defaultProfile) {
        this.defaultProfile = defaultProfile;
    }

    @Override
    public String toString(){
        return name+" - "+description;
    }

    @Override
    public boolean equals(Object obj){
        if (obj == null) {
            return false;
        }
        if (!Profile.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final Profile other = (Profile) obj;
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
