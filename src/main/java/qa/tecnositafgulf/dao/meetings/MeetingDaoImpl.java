package qa.tecnositafgulf.dao.meetings;

import org.springframework.stereotype.Repository;
import qa.tecnositafgulf.model.administration.Employee;
import qa.tecnositafgulf.model.meetings.Meeting;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

/**
 * Created by hadi on 1/7/18.
 */
@Repository
public class MeetingDaoImpl implements MeetingDao {

    @PersistenceContext
    EntityManager em;
    public void save(Meeting meeting) throws Exception {
        em.merge(meeting);
    }

    public void remove(Meeting meeting) {
        em.remove(em.contains(meeting)?meeting:em.merge(meeting));
    }

    public List<Meeting> listAll() {
        TypedQuery<Meeting> query = em.createNamedQuery("Meeting.listAll", Meeting.class);
        return query.getResultList();
    }

    public List<Meeting> listAllByPartecipant(Employee employee) {
        TypedQuery<Meeting> query = em.createNamedQuery("Meeting.listAllByPartecipant", Meeting.class);
        query.setParameter("employee", employee);
        return query.getResultList();
    }

    public Meeting findByTitleAndFrom(String title, Date fromTime, Employee employee){
        Meeting m = null;
        try {
            TypedQuery<Meeting> query = em.createNamedQuery("Meeting.findByTitleAndFromAndPartecipant", Meeting.class);
            query.setParameter("title", title);
            query.setParameter("fromTime", fromTime);
            query.setParameter("employee", employee);
            m = query.getSingleResult();
        }catch (NoResultException e){
            //ignore
        }
        return m;
    }

    @Override
    public void updateMeeting(Meeting meeting, Date updateFromTime, Date updateToTime, Employee employee) {
        meeting.setFromTime(updateFromTime);
        meeting.setToTime(updateToTime);
        em.merge(meeting);

    }
}
