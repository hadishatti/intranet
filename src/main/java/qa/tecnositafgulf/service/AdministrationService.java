package qa.tecnositafgulf.service;

import org.hibernate.exception.ConstraintViolationException;
import qa.tecnositafgulf.model.administration.*;
import qa.tecnositafgulf.searchcriteria.*;

import java.util.List;


public interface AdministrationService {

    String MD5(String plain);


    void saveProfile(Profile profile);

    void removeProfile(Profile profile) throws ConstraintViolationException;

    List<Profile> listProfiles();

    Profile getDefaultProfile();

    Integer getUserProfileCount(ProfileSearchCriteria profileSearchCriteria);

    List<Profile> getProfileList(ProfileSearchCriteria profileSearchCriteria);

    Profile getProfileByName(String name);

    int countProfiles();

    Employee findEmployeeByEmployeeID(String employeeID);

    void saveEmployee(Employee employee);

    void removeEmployee(Employee employee) throws ConstraintViolationException;

    List<Employee> listEmployees();

    List<Employee> listEmployeesByManager(Employee manager);

    List<Employee> listEmployeesByHRManager(Employee manager);

    List<Employee> listEmployeesByFinanceManager(Employee manager);

    List<Employee> listEmployeesByTicketManager(Employee manager);

    List<Employee> listEmployeesByChartId();

    Integer getEmployeeCount(EmployeeSearchCriteria employeeSearchCriteria);

    List<Employee> getEmployeeList(EmployeeSearchCriteria employeeSearchCriteria);

    Integer getEmployeesCount(LeaveManagerSearchCriteria leaveManagerSearchCriteria);

    List<Employee> getAllEmployees(LeaveManagerSearchCriteria leaveManagerSearchCriteria);

    List<Employee> listLeaveManagers();

    List<Employee> listHRLeaveManagers();

    List<Employee> listFinanceLeaveManagers();

    List<Employee> listTicketLeaveManagers();

    int countEmployees();


    void saveRole(Role role);

    void removeRole(Role role) throws ConstraintViolationException;

    List<Role> listRoles();

    Integer getRolesCount(RoleSearchCriteria roleSearchCriteria);

    List<Role> getRolesList(RoleSearchCriteria roleSearchCriteria);

    int countRoles();


    void saveDepartment(Department department);

    void removeDepartment(Department department) throws ConstraintViolationException;

    Integer getDepartmentCount(DepartmentsSearchCriteira departmentsSearchCriteira);

    List<Department> getDepartmentList(DepartmentsSearchCriteira departmentsSearchCriteira);

    List<Department> listDepartments();

    List<Department> listDepartmentsByChartId();

    int countDepartments();


    void saveCompany(Company company);

    void removeCompany(Company company) throws ConstraintViolationException;

    List<Company> listCompanies();

    Integer getCompanyCount(CompanySearchCriteria companySearchCriteria);

    List<Company> getCompanyList(CompanySearchCriteria companySearchCriteria);

    int countCompanies();

}
