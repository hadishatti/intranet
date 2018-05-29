package qa.tecnositafgulf.model.helpDesk;

import qa.tecnositafgulf.model.administration.Employee;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by klajdi on 19/05/18.
 */

@Entity
@Table(name = "helpdesk", uniqueConstraints = {@UniqueConstraint(columnNames = {"title"})})
@NamedQueries({
        @NamedQuery(name = "HelpDesk.listAll", query = "SELECT helpdesk FROM HelpDesk helpdesk ORDER BY helpdesk.date DESC"),
})
public class HelpDesk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date date;
    private String title;

    @Lob
    private String content;

    @OneToOne
    @JoinColumn(name = "authorID")
    private Employee author;

    @Lob
    private String image;

    private int imageWidth;


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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
}
