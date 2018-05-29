package qa.tecnositafgulf.dao.helpDesk;

import org.springframework.stereotype.Repository;
import qa.tecnositafgulf.model.helpDesk.HelpDesk;
import qa.tecnositafgulf.searchcriteria.helpDesk.HelpDeskSearchCriteria;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by klajdi on 20/05/18.
 */

@Repository
public class HelpDeskDaoImpl implements HelpDeskDao{

    @PersistenceContext
    private EntityManager em;

    public void save(HelpDesk helpDesk){
        em.merge(helpDesk);
    }

    public void remove(HelpDesk helpDesk){
        em.remove(em.contains(helpDesk) ? helpDesk : em.merge(helpDesk));
    }

    public List<HelpDesk> listAll(){
        TypedQuery<HelpDesk> query = em.createNamedQuery("HelpDesk.listAll", HelpDesk.class);
        return query.getResultList();
    }

    @Override
    public List<HelpDesk> listHelpDesks(HelpDeskSearchCriteria helpDeskSearchCriteria) {
        Query query = helpDeskSearchCriteria.toQuery(em);
        return query.getResultList();
    }

    @Override
    public Integer countHelpDesk(HelpDeskSearchCriteria helpDeskSearchCriteria) {
        Query query = helpDeskSearchCriteria.getCountQuery(em);
        Number cResults = (Number) query.getSingleResult();
        return cResults.intValue();
    }
}
