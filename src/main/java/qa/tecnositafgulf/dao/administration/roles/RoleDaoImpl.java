package qa.tecnositafgulf.dao.administration.roles;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Repository;
import qa.tecnositafgulf.model.administration.Role;
import qa.tecnositafgulf.searchcriteria.RoleSearchCriteria;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class RoleDaoImpl implements RoleDao{

    @PersistenceContext
    private EntityManager em;

    public void save(Role role) {
        em.merge(role);
    }

    public void remove(Role role)  throws ConstraintViolationException {
        em.remove(em.contains(role) ? role : em.merge(role));
    }

    public List<Role> listAll() {
        TypedQuery<Role> query = em.createNamedQuery("Role.listAll", Role.class);
        return query.getResultList();
    }

    public int countAll() {
        Query query = em.createNamedQuery("Role.countAll");
        return ((Long)query.getSingleResult()).intValue();
    }

    @Override
    public Integer getRolesCount(RoleSearchCriteria roleSearchCriteria) {
        Query query = roleSearchCriteria.getCountQuery(em);
        Number cResults = (Number) query.getSingleResult();
        return cResults.intValue();
    }

    @Override
    public List<Role> getRolesList(RoleSearchCriteria roleSearchCriteria) {
        Query query = roleSearchCriteria.toQuery(em);
        return query.getResultList();
    }
}
