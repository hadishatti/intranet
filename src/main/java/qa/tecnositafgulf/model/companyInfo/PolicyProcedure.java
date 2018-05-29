package qa.tecnositafgulf.model.companyInfo;

import qa.tecnositafgulf.model.administration.Employee;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by ledio on 5/16/18.
 */

@Entity
@Table(name = "policyProcedure")
@NamedQueries({
        @NamedQuery(name = "PolicyProcedure.listAll", query = "SELECT policyProcedure FROM PolicyProcedure policyProcedure ORDER BY policyProcedure.date DESC"),
        @NamedQuery(name = "PolicyProcedure.find", query = "SELECT policyProcedure FROM PolicyProcedure policyProcedure WHERE policyProcedure = :policyProcedure")
})
public class PolicyProcedure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String procedureId;

    private Date date;

    @Lob
    private String content;

    @OneToOne
    @JoinColumn(name = "authorID")
    private Employee author;

    private String title;

    @Column(unique = true)
    private String number;

    private String type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Employee getAuthor() {
        return author;
    }

    public void setAuthor(Employee author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProcedureId() {
        return procedureId;
    }

    public void setProcedureId(String procedureId) {
        this.procedureId = procedureId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PolicyProcedure tender = (PolicyProcedure) o;

        if (!id.equals(tender.id)) return false;
        return number.equals(tender.number);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + number.hashCode();
        return result;
    }
}
