package qa.tecnositafgulf.dao.events;

import org.springframework.stereotype.Repository;
import qa.tecnositafgulf.model.events.CompanyEvent;
import qa.tecnositafgulf.searchcriteria.companyevents.CompanyEventsSearchCriteria;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by hadi on 12/24/17.
 */
@Repository
public class EventDaoImpl implements EventDao{

    @PersistenceContext
    private EntityManager em;

    public void save(CompanyEvent companyEvent) {
        em.merge(companyEvent);
    }

    public void remove(CompanyEvent companyEvent) {
        em.remove(em.contains(companyEvent) ? companyEvent : em.merge(companyEvent));
    }

    public List<CompanyEvent> listAll() {
        TypedQuery<CompanyEvent> query = em.createNamedQuery("CompanyEvent.listAll", CompanyEvent.class);
        return query.getResultList();
    }

    public CompanyEvent find(CompanyEvent companyEvent){
        TypedQuery<CompanyEvent> query = em.createNamedQuery("CompanyEvent.find", CompanyEvent.class);
        query.setParameter("companyEvent", companyEvent);
        return query.getSingleResult();
    }

    @Override
    public List<CompanyEvent> listEvents(CompanyEventsSearchCriteria companyEventsSearchCriteria) {
        Query query = companyEventsSearchCriteria.toQuery(em);
        return query.getResultList();
    }

    @Override
    public Integer getCountCompanyEvents(CompanyEventsSearchCriteria companyEventsSearchCriteria) {
        Query query = companyEventsSearchCriteria.getCountQuery(em);
        Number cResults = (Number) query.getSingleResult();
        return cResults.intValue();
    }
}
