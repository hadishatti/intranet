package qa.tecnositafgulf.dao.events;

import org.springframework.stereotype.Repository;
import qa.tecnositafgulf.model.events.CompanyEvent;
import qa.tecnositafgulf.model.events.EventComment;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by hadi on 12/24/17.
 */
@Repository
public class EventCommentDaoImpl implements EventCommentDao {

    @PersistenceContext
    private EntityManager em;

    public void save(EventComment eventComment) {
        em.merge(eventComment);
    }

    public void remove(EventComment eventComment) {
        em.remove(em.contains(eventComment) ? eventComment : em.merge(eventComment));
    }

    public List<EventComment> listByEvent(CompanyEvent companyEvent) {
        TypedQuery<EventComment> query = em.createNamedQuery("EventComment.listByEvent", EventComment.class);
        query.setParameter("companyEvent", companyEvent);
        return query.getResultList();
    }
}
