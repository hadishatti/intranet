package qa.tecnositafgulf.dao.administration.employees;

import org.hibernate.exception.ConstraintViolationException;
import qa.tecnositafgulf.model.administration.Employee;
import qa.tecnositafgulf.searchcriteria.EmployeeSearchCriteria;
import qa.tecnositafgulf.searchcriteria.LeaveManagerSearchCriteria;

import java.util.List;

;

/**
 * Created by hadi on 12/17/17.
 */
public interface EmployeeDao {

    void save(Employee employee);

    void remove(Employee employee) throws ConstraintViolationException;

    List<Employee> listAll();

    List<Employee> listAllOrderByChartId();

    List<Employee> listAllEmployeeByManager(Employee manager);

    List<Employee> listAllEmployeeByHRManager(Employee manager);

    List<Employee> listAllEmployeeByFinanceManager(Employee manager);

    List<Employee> listAllEmployeeByTicketManager(Employee manager);

    int countAll();

    Integer getEmployeeCount(EmployeeSearchCriteria employeeSearchCriteria);

    List<Employee> getEmployeeList(EmployeeSearchCriteria employeeSearchCriteria);

    Integer getEmployeesCount(LeaveManagerSearchCriteria leaveManagerSearchCriteria);

    List<Employee> getAllEmployees(LeaveManagerSearchCriteria leaveManagerSearchCriteria);

    Employee findByUsername(String username);

    String MD5(String plain);

    Employee findByEmployeeID(String employeeID);

    List<Employee> listLeaveManagers();

    List<Employee> listHRLeaveManagers();

    List<Employee> listFinanceLeaveManagers();

    List<Employee> listTicketLeaveManagers();
}