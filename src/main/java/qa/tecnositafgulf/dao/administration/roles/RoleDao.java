package qa.tecnositafgulf.dao.administration.roles;

import org.hibernate.exception.ConstraintViolationException;
import qa.tecnositafgulf.model.administration.Role;
import qa.tecnositafgulf.searchcriteria.RoleSearchCriteria;

import java.util.List;

/**
 * Created by hadi on 12/17/17.
 */
public interface RoleDao {
    void save(Role role);

    void remove(Role role) throws ConstraintViolationException;

    List<Role> listAll();

    int countAll();

    Integer getRolesCount(RoleSearchCriteria roleSearchCriteria);

    List<Role> getRolesList(RoleSearchCriteria roleSearchCriteria);
}
