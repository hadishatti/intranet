package qa.tecnositafgulf.dao.tenders;

import org.springframework.stereotype.Repository;
import qa.tecnositafgulf.model.tenders.Tender;
import qa.tecnositafgulf.searchcriteria.tender.TenderSearchCriteria;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by hadi on 12/24/17.
 */
@Repository
public class TenderDaoImpl implements TenderDao{

    @PersistenceContext
    private EntityManager em;

    public void save(Tender tender) {
        em.merge(tender);
    }

    public void remove(Tender tender) {
        em.remove(em.contains(tender) ? tender : em.merge(tender));
    }

    public List<Tender> listAll() {
        TypedQuery<Tender> query = em.createNamedQuery("Tender.listAll", Tender.class);
        return query.getResultList();
    }

    public Tender find(Tender tender){
        TypedQuery<Tender> query = em.createNamedQuery("Tender.find", Tender.class);
        query.setParameter("tender", tender);
        return query.getSingleResult();
    }

    @Override
    public List<Tender> listTenders(TenderSearchCriteria tenderSearchCriteria) {
        Query query = tenderSearchCriteria.toQuery(em);
        return query.getResultList();
    }

    @Override
    public Integer countTender(TenderSearchCriteria tenderSearchCriteria) {
        Query query = tenderSearchCriteria.getCountQuery(em);
        Number cResults = (Number) query.getSingleResult();
        return cResults.intValue();
    }
}
