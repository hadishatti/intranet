package qa.tecnositafgulf.model.tenders;

import qa.tecnositafgulf.model.administration.Employee;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tender",  uniqueConstraints = {@UniqueConstraint(columnNames={"number"})})
@NamedQueries({
        @NamedQuery(name = "Tender.listAll", query = "SELECT tender FROM Tender tender ORDER BY tender.date DESC"),
        @NamedQuery(name = "Tender.find", query = "SELECT tender FROM Tender tender WHERE tender = :tender")
})
public class Tender {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date date;

    private Date issuingDate;
    private Date closingDate;

    @Lob
    private String content;

    @OneToOne
    @JoinColumn(name = "authorID")
    private Employee author;

    @OneToMany(mappedBy = "tender", fetch = FetchType.LAZY)
    private List<TenderComment> tenderComments;

    private String title;

    private String number;

    private String type;

    private String link;

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

    public Date getIssuingDate() {
        return issuingDate;
    }

    public void setIssuingDate(Date issuingDate) {
        this.issuingDate = issuingDate;
    }

    public Date getClosingDate() {
        return closingDate;
    }

    public void setClosingDate(Date closingDate) {
        this.closingDate = closingDate;
    }

    public Employee getAuthor() {
        return author;
    }

    public void setAuthor(Employee author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<TenderComment> getTenderComments() {
        return tenderComments;
    }

    public void setTenderComments(List<TenderComment> tenderComments) {
        this.tenderComments = tenderComments;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tender tender = (Tender) o;

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
