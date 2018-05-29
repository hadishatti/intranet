package qa.tecnositafgulf.dao.administration.departments;

import org.hibernate.exception.ConstraintViolationException;
import qa.tecnositafgulf.model.administration.Department;
import qa.tecnositafgulf.searchcriteria.DepartmentsSearchCriteira;

import java.util.List;

/**
 * Created by hadi on 12/17/17.
 */
public interface DepartmentDao {

    void save(Department department);

    void remove(Department department) throws ConstraintViolationException;

    List<Department> listAll();

    List<Department> listAllOrderByChartId();

    Integer getDepartmentCount(DepartmentsSearchCriteira departmentsSearchCriteira);

    List<Department> getDepartmentList(DepartmentsSearchCriteira departmentsSearchCriteira);

    int countAll();
}