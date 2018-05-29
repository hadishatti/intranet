package qa.tecnositafgulf.service;

import qa.tecnositafgulf.model.administration.Employee;
import qa.tecnositafgulf.model.meetings.Meeting;

import java.util.Date;
import java.util.List;

/**
 * Created by hadi on 1/7/18.
 */
public interface MeetingService {

    public void saveMeeting(Meeting meeting)  throws Exception;
    public void removeMeeting(Meeting meeting);
    public List<Meeting> listAllMeetings();
    public List<Meeting> listAllEmployeeMeetings(Employee employee);
    public Meeting findMeetingByTitleAndFrom(String title, Date fromTime, Employee employee);
    public void updateMeeting(Meeting meeting, Date updateFromTime, Date updateToTime, Employee employee);
}
