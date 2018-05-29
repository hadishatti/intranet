package qa.tecnositafgulf.dao.companyInfo;

import org.springframework.stereotype.Repository;
import qa.tecnositafgulf.model.companyInfo.Partner;
import qa.tecnositafgulf.searchcriteria.PartnerSearchCriteria;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by ameljo on 05/05/18.
 */

@Repository
public class PartnerDaoImpl implements PartnerDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void save(Partner partner) {
        em.merge(partner);
    }

    @Override
    public void remove(Partner partner) {
        em.remove((em.contains(partner) ? partner : em.merge(partner)));
    }

    @Override
    public List<Partner> listAll() {
        TypedQuery<Partner> query = em.createNamedQuery("Partner.listAll", Partner.class);
        return query.getResultList();
    }

    @Override
    public List<Partner> getPartnersList(PartnerSearchCriteria searchCriteria) {
        Query query = searchCriteria.toQuery(em);
        return query.getResultList();
    }

    @Override
    public int getPartnersCount(PartnerSearchCriteria searchCriteria) {
        Query query = searchCriteria.getCountQuery(em);
        Number count = (Number) query.getSingleResult();
        return count.intValue();
    }

    @Override
    public int countAll() {
        return 0;
    }
}
