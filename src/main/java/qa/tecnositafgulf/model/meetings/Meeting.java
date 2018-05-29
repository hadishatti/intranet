package qa.tecnositafgulf.model.meetings;

import qa.tecnositafgulf.model.administration.Employee;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by hadi on 1/7/18.
 */
@Entity
@Table(name = "meeting", uniqueConstraints = {@UniqueConstraint(columnNames={"title", "fromTime"})})
@NamedQueries({
        @NamedQuery(name = "Meeting.listAll", query = "SELECT meeting FROM Meeting meeting"),
        @NamedQuery(name = "Meeting.findByTitleAndFromAndPartecipant", query = "SELECT meeting FROM Meeting meeting JOIN meeting.partecipants partecipant WHERE partecipant = :employee AND meeting.title = :title AND meeting.fromTime=:fromTime"),
        @NamedQuery(name = "Meeting.listAllByPartecipant", query = "SELECT meeting FROM Meeting meeting JOIN meeting.partecipants partecipant WHERE partecipant = :employee"),
        @NamedQuery(name = "Meeting.updateMeeting", query = "Update Meeting meeting set meeting.fromTime =:updateFromTime, meeting.toTime=:updateToTime where meeting.partecipants=:employee and meeting.title=:title")
})
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(
            name="employee_meetings",
            joinColumns=@JoinColumn(name="meetingID", referencedColumnName="id"),
            inverseJoinColumns=@JoinColumn(name="employeeID", referencedColumnName="id")
    )
    private List<Employee> partecipants;
    private Date fromTime;
    private Date toTime;
    private String title;
    private String location;
    private String headerColor;
    private String contentColor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Employee> getPartecipants() {
        return partecipants;
    }

    public void setPartecipants(List<Employee> partecipants) {
        this.partecipants = partecipants;
    }

    public Date getFromTime() {
        return fromTime;
    }

    public void setFromTime(Date fromTime) {
        this.fromTime = fromTime;
    }

    public Date getToTime() {
        return toTime;
    }

    public void setToTime(Date toTime) {
        this.toTime = toTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getHeaderColor() {
        return headerColor;
    }

    public void setHeaderColor(String headerColor) {
        this.headerColor = headerColor;
    }

    public String getContentColor() {
        return contentColor;
    }

    public void setContentColor(String contentColor) {
        this.contentColor = contentColor;
    }

}
