package qa.tecnositafgulf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qa.tecnositafgulf.dao.meetings.MeetingDao;
import qa.tecnositafgulf.model.administration.Employee;
import qa.tecnositafgulf.model.meetings.Meeting;

import java.util.Date;
import java.util.List;

/**
 * Created by hadi on 1/7/18.
 */
@Service
@Transactional
public class MeetingServiceImpl implements MeetingService {

    @Autowired
    MeetingDao dao;

    public void saveMeeting(Meeting meeting) throws Exception {
        dao.save(meeting);
    }

    public void removeMeeting(Meeting meeting) {
        dao.remove(meeting);
    }

    public List<Meeting> listAllMeetings() {
        return dao.listAll();
    }

    public List<Meeting> listAllEmployeeMeetings(Employee employee) {
        return dao.listAllByPartecipant(employee);
    }

    public Meeting findMeetingByTitleAndFrom(String title, Date fromTime, Employee employee){
        return dao.findByTitleAndFrom(title, fromTime, employee);
    }

    @Override
    public void updateMeeting(Meeting meeting, Date updateFromTime, Date updateToTime, Employee employee) {
        dao.updateMeeting(meeting, updateFromTime, updateToTime, employee);
    }
}
