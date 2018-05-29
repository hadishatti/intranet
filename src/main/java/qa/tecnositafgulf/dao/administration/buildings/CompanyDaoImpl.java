package qa.tecnositafgulf.dao.administration.buildings;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Repository;
import qa.tecnositafgulf.model.administration.Company;
import qa.tecnositafgulf.searchcriteria.CompanySearchCriteria;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class CompanyDaoImpl implements CompanyDao {

    @PersistenceContext
    private EntityManager em;

    public void save(Company company) {
        em.merge(company);
    }

    public void remove(Company company) throws ConstraintViolationException {
        em.remove(em.contains(company) ? company : em.merge(company));
    }

    public List<Company> listAll() {
        TypedQuery<Company> query = em.createNamedQuery("Company.listAll", Company.class);
        return query.getResultList();
    }

    public int countAll() {
        Query query = em.createNamedQuery("Company.countAll");
        return ((Long)query.getSingleResult()).intValue();
    }

    @Override
    public Integer getCompanyCount(CompanySearchCriteria companySearchCriteria) {
        Query query = companySearchCriteria.getCountQuery(em);
        Number cResults = (Number) query.getSingleResult();
        return cResults.intValue();
    }

    @Override
    public List<Company> getCompanyList(CompanySearchCriteria companySearchCriteria) {
        Query query = companySearchCriteria.toQuery(em);
        return query.getResultList();
    }
}
