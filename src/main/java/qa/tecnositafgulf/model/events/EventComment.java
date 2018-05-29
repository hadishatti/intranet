package qa.tecnositafgulf.model.events;

import qa.tecnositafgulf.model.administration.Employee;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "event_comment")
@NamedQueries({
        @NamedQuery(name = "EventComment.listByEvent", query = "SELECT comment FROM EventComment comment WHERE comment.companyEvent = :companyEvent ORDER BY comment.date DESC")
})
public class EventComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date date;
    private String content;

    @OneToOne
    @JoinColumn(name = "authorID")
    private Employee author;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "eventID")
    private CompanyEvent companyEvent;

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

    public CompanyEvent getCompanyEvent() {
        return companyEvent;
    }

    public void setCompanyEvent(CompanyEvent companyEvent) {
        this.companyEvent = companyEvent;
    }

    public Employee getAuthor() {
        return author;
    }

    public void setAuthor(Employee author) {
        this.author = author;
    }
}
