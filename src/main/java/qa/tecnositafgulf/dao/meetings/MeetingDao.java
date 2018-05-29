package qa.tecnositafgulf.dao.meetings;

import qa.tecnositafgulf.model.administration.Employee;
import qa.tecnositafgulf.model.meetings.Meeting;

import java.util.Date;
import java.util.List;

/**
 * Created by hadi on 1/7/18.
 */
public interface MeetingDao {

    public void save(Meeting meeting)  throws Exception;
    public void remove(Meeting meeting);
    public List<Meeting> listAll();

    List<Meeting> listAllByPartecipant(Employee employee);
    public Meeting findByTitleAndFrom(String title, Date fromTime, Employee employee);
    public void updateMeeting(Meeting meeting, Date updateFromTime, Date updateToTime, Employee employee);

}
