package qa.tecnositafgulf.dao.companyInfo.policiesProcedures;

import org.springframework.stereotype.Repository;
import qa.tecnositafgulf.model.companyInfo.PolicyProcedure;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by ledio on 5/16/18.
 */

@Repository
public class PolicyProcedureDaoImpl implements PolicyProcedureDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void save(PolicyProcedure policyProcedure) {
        em.merge(policyProcedure);
    }

    @Override
    public void remove(PolicyProcedure policyProcedure) {
        em.remove(em.contains(policyProcedure) ? policyProcedure : em.merge(policyProcedure));
    }

    @Override
    public List<PolicyProcedure> listAll() {
        TypedQuery<PolicyProcedure> query = em.createNamedQuery("PolicyProcedure.listAll", PolicyProcedure.class);
        return query.getResultList();
    }

    @Override
    public PolicyProcedure find(PolicyProcedure policyProcedure) {
        TypedQuery<PolicyProcedure> query = em.createNamedQuery("PolicyProcedure.find", PolicyProcedure.class);
        query.setParameter("policyProcedure", policyProcedure);
        return query.getSingleResult();
    }
}
