package qa.tecnositafgulf.model.tenders;

import qa.tecnositafgulf.model.administration.Employee;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tender_comment")
@NamedQueries({
        @NamedQuery(name = "TenderComment.listByTender", query = "SELECT comment FROM TenderComment comment WHERE comment.tender = :tender ORDER BY comment.date DESC")
})
public class TenderComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date date;
    private String content;

    @OneToOne
    @JoinColumn(name = "authorID")
    private Employee author;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tenderID")
    private Tender tender;

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

    public Tender getTender() {
        return tender;
    }

    public void setTender(Tender tender) {
        this.tender = tender;
    }

    public Employee getAuthor() {
        return author;
    }

    public void setAuthor(Employee author) {
        this.author = author;
    }
}
