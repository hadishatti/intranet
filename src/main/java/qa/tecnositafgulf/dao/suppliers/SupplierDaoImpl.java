package qa.tecnositafgulf.dao.suppliers;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import qa.tecnositafgulf.model.suppliers.Supplier;
import qa.tecnositafgulf.searchcriteria.SupplierSearchCriteria;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by ledio on 5/5/18.
 */
@Repository
public class SupplierDaoImpl implements SupplierDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void save(Supplier supplier) throws ConstraintViolationException {
        em.merge(supplier);
    }

    @Override
    @Transactional
    public void remove(Supplier supplier) throws ConstraintViolationException {
        em.remove(em.contains(supplier) ? supplier : em.merge(supplier));
    }

    @Override
    public List<Supplier> listAll() {
        TypedQuery<Supplier> query = em.createNamedQuery("Supplier.listAll", Supplier.class);
        return query.getResultList();
    }

    @Override
    public Supplier findByName(String name) {
        Query query = em.createNamedQuery("Supplier.findByName");
        query.setParameter("name",name);
        Supplier supplier = (Supplier) query.getSingleResult();
        return supplier;
    }

    @Override
    public List<Supplier> getAll(SupplierSearchCriteria supplierSearchCriteria) {
        Query query = supplierSearchCriteria.toQuery(em);
        return query.getResultList();
    }

    @Override
    public Integer countAll(SupplierSearchCriteria supplierSearchCriteria) {
        Query query = supplierSearchCriteria.getCountQuery(em);
        Number cResults = (Number) query.getSingleResult();
        return cResults.intValue();
    }
}
