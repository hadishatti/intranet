package qa.tecnositafgulf.dao.administration.departments;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Repository;
import qa.tecnositafgulf.model.administration.Department;
import qa.tecnositafgulf.searchcriteria.DepartmentsSearchCriteira;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class DepartmentDaoImpl implements DepartmentDao{

    @PersistenceContext
    private EntityManager em;

    public void save(Department department) {
        em.merge(department);
    }

    public void remove(Department department)  throws ConstraintViolationException {
        em.remove(em.contains(department) ? department : em.merge(department));
    }

    public List<Department> listAll() {
        TypedQuery<Department> query = em.createNamedQuery("Department.listAll", Department.class);
        return query.getResultList();
    }

    public List<Department> listAllOrderByChartId() {
        TypedQuery<Department> query = em.createNamedQuery("Department.listAllOrderByChartId", Department.class);
        return query.getResultList();
    }

    @Override
    public Integer getDepartmentCount(DepartmentsSearchCriteira departmentsSearchCriteira) {
        Query query = departmentsSearchCriteira.getCountQuery(em);
        Number cResults = (Number) query.getSingleResult();
        return cResults.intValue();
    }

    @Override
    public List<Department> getDepartmentList(DepartmentsSearchCriteira departmentsSearchCriteira) {
        Query query = departmentsSearchCriteira.toQuery(em);
        return query.getResultList();
    }

    public int countAll() {
        Query query = em.createNamedQuery("Department.countAll");
        return ((Long)query.getSingleResult()).intValue();
    }
}
