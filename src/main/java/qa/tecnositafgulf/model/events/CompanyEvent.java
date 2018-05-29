package qa.tecnositafgulf.model.events;

import qa.tecnositafgulf.model.administration.Employee;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "companyEvent")
@NamedQueries({
        @NamedQuery(name = "CompanyEvent.listAll", query = "SELECT companyEvent FROM CompanyEvent companyEvent ORDER BY companyEvent.date DESC"),
        @NamedQuery(name = "CompanyEvent.find", query = "SELECT companyEvent FROM CompanyEvent companyEvent WHERE companyEvent = :companyEvent")
})
public class CompanyEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date date;

    @Lob
    private String content;

    @OneToOne
    @JoinColumn(name = "authorID")
    private Employee author;

    @OneToMany(mappedBy = "companyEvent", fetch = FetchType.LAZY)
    private List<EventComment> eventComments;

    @Lob
    private String image;

    private int imageWidth;

    private String title;

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

    public List<EventComment> getEventComments() {
        return eventComments;
    }

    public void setEventComments(List<EventComment> eventComments) {
        this.eventComments = eventComments;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CompanyEvent companyEvent = (CompanyEvent) o;

        if (!id.equals(companyEvent.id)) return false;
        if (!date.equals(companyEvent.date)) return false;
        return author.equals(companyEvent.author);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + date.hashCode();
        result = 31 * result + author.hashCode();
        return result;
    }
}
