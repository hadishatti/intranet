package qa.tecnositafgulf.dao.administration.employees;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Repository;
import qa.tecnositafgulf.model.administration.Employee;
import qa.tecnositafgulf.searchcriteria.EmployeeSearchCriteria;
import qa.tecnositafgulf.searchcriteria.LeaveManagerSearchCriteria;

import javax.persistence.*;
import java.util.List;

@Repository
public class EmployeeDaoImpl implements EmployeeDao{

    @PersistenceContext
    private EntityManager em;

    public void save(Employee employee) {
        em.merge(employee);
    }

    public void remove(Employee employee) throws ConstraintViolationException {
        em.remove(em.contains(employee) ? employee : em.merge(employee));
    }

    public List<Employee> listAll() {
        TypedQuery<Employee> query = em.createNamedQuery("Employee.listAll", Employee.class);
        return query.getResultList();
    }

    public List<Employee> listAllOrderByChartId() {
        TypedQuery<Employee> query = em.createNamedQuery("Employee.listAllOrderByChartId", Employee.class);
        return query.getResultList();
    }

    public int countAll() {
        Query query = em.createNamedQuery("Employee.countAll");
        return ((Long)query.getSingleResult()).intValue();
    }

    @Override
    public Integer getEmployeeCount(EmployeeSearchCriteria employeeSearchCriteria) {
        Query query = employeeSearchCriteria.getCountQuery(em);
        Number cResults = (Number) query.getSingleResult();
        return cResults.intValue();
    }

    @Override
    public List<Employee> getEmployeeList(EmployeeSearchCriteria employeeSearchCriteria) {
        Query query = employeeSearchCriteria.toQuery(em);
        return query.getResultList();
    }

    @Override
    public List<Employee> listAllEmployeeByManager(Employee manager){
        TypedQuery<Employee> query = em.createNamedQuery("Employee.listByLeaveManager", Employee.class);
        query.setParameter("manager",manager);
        return query.getResultList();
    }

    @Override
    public List<Employee> listAllEmployeeByHRManager(Employee manager){
        TypedQuery<Employee> query = em.createNamedQuery("Employee.listByHRLeaveManager", Employee.class);
        query.setParameter("manager",manager);
        return query.getResultList();
    }

    @Override
    public List<Employee> listAllEmployeeByFinanceManager(Employee manager){
        TypedQuery<Employee> query = em.createNamedQuery("Employee.listByFinanceLeaveManager", Employee.class);
        query.setParameter("manager",manager);
        return query.getResultList();
    }

    @Override
    public List<Employee> listAllEmployeeByTicketManager(Employee manager){
        TypedQuery<Employee> query = em.createNamedQuery("Employee.listByTicketLeaveManager", Employee.class);
        query.setParameter("manager",manager);
        return query.getResultList();
    }

    @Override
    public Integer getEmployeesCount(LeaveManagerSearchCriteria leaveManagerSearchCriteria) {
        Query query = leaveManagerSearchCriteria.getCountQuery(em);
        Number cResults = (Number) query.getSingleResult();
        return cResults.intValue();
    }

    @Override
    public List<Employee> getAllEmployees(LeaveManagerSearchCriteria leaveManagerSearchCriteria) {
        Query query = leaveManagerSearchCriteria.toQuery(em);
        return query.getResultList();
    }

    @Override
    public Employee findByUsername(String username) {
        Query query = em.createNamedQuery("Employee.findByUsername");
        query.setParameter("username",username);
        Employee employee = (Employee) query.getSingleResult();
        return employee;
    }

    @Override
    public Employee findByEmployeeID(String employeeID) {
        Query query = em.createNamedQuery("Employee.findByEmployeeID");
        query.setParameter("employeeNumber",employeeID);
        Employee employee;
        try {
           employee = (Employee) query.getSingleResult();
        } catch (NoResultException e){
            employee=null;
        }
        return employee;
    }

    public List<Employee> listLeaveManagers() {
        TypedQuery<Employee> query = em.createNamedQuery("Employee.listLeaveManagers", Employee.class);
        return query.getResultList();
    }

    public List<Employee> listHRLeaveManagers() {
        TypedQuery<Employee> query = em.createNamedQuery("Employee.listHRLeaveManagers", Employee.class);
        return query.getResultList();
    }

    public List<Employee> listFinanceLeaveManagers() {
        TypedQuery<Employee> query = em.createNamedQuery("Employee.listFinanceLeaveManagers", Employee.class);
        return query.getResultList();
    }

    public List<Employee> listTicketLeaveManagers() {
        TypedQuery<Employee> query = em.createNamedQuery("Employee.listTicketLeaveManagers", Employee.class);
        return query.getResultList();
    }


    public String MD5(String plain) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(plain.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }
}
