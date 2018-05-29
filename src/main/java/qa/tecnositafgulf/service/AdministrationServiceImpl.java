package qa.tecnositafgulf.service;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qa.tecnositafgulf.dao.administration.buildings.CompanyDao;
import qa.tecnositafgulf.dao.administration.departments.DepartmentDao;
import qa.tecnositafgulf.dao.administration.employees.EmployeeDao;
import qa.tecnositafgulf.dao.administration.profiles.ProfileDao;
import qa.tecnositafgulf.dao.administration.roles.RoleDao;
import qa.tecnositafgulf.model.administration.*;
import qa.tecnositafgulf.searchcriteria.*;

import java.util.List;

@Service
public class AdministrationServiceImpl implements AdministrationService {

  
    @Autowired
    private ProfileDao profileDao;

    @Autowired
    private EmployeeDao employeeDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private CompanyDao companyDao;

    @Transactional
    public int countUsers() {
        return employeeDao.countAll();
    }

    public String MD5(String plain) {
        return employeeDao.MD5(plain);
    }

    @Transactional
    public void saveProfile(Profile profile) {
        profileDao.save(profile);
    }

    @Transactional
    public void removeProfile(Profile profile) throws ConstraintViolationException {
        profileDao.remove(profile);
    }

    @Transactional
    public List<Profile> listProfiles() {
        return profileDao.listAll();
    }

    @Transactional
    public int countProfiles() {
        return profileDao.countAll();
    }

    @Transactional
    public Profile getDefaultProfile(){
        return profileDao.getDefaultProfile();
    }

    @Override
    @Transactional
    public Integer getUserProfileCount(ProfileSearchCriteria profileSearchCriteria) {
        return profileDao.getProfileCount(profileSearchCriteria);
    }

    @Override
    @Transactional
    public List<Profile> getProfileList(ProfileSearchCriteria profileSearchCriteria) {
        return profileDao.getProfileList(profileSearchCriteria);
    }

    @Override
    public Profile getProfileByName(String name) {
        return profileDao.findByName(name);
    }

    @Transactional
    public void saveEmployee(Employee employee) {
        employeeDao.save(employee);
    }

    @Transactional
    public void removeEmployee(Employee employee) throws ConstraintViolationException {
        employeeDao.remove(employee);
    }

    @Transactional
    public Employee findEmployeeByEmployeeID(String employeeID) {
        return employeeDao.findByEmployeeID(employeeID);
    }

    @Transactional
    public List<Employee> listEmployees() {
        return employeeDao.listAll();
    }

    @Transactional
    public List<Employee> listEmployeesByChartId(){
        return employeeDao.listAllOrderByChartId();
    }


    @Override
    @Transactional
    public Integer getEmployeeCount(EmployeeSearchCriteria employeeSearchCriteria) {
        return employeeDao.getEmployeeCount(employeeSearchCriteria);
    }

    @Override
    @Transactional
    public List<Employee> getEmployeeList(EmployeeSearchCriteria employeeSearchCriteria) {
        return employeeDao.getEmployeeList(employeeSearchCriteria);
    }

    @Override
    @Transactional
    public Integer getEmployeesCount(LeaveManagerSearchCriteria leaveManagerSearchCriteria) {
        return employeeDao.getEmployeesCount(leaveManagerSearchCriteria);
    }

    @Override
    @Transactional
    public List<Employee> getAllEmployees(LeaveManagerSearchCriteria leaveManagerSearchCriteria) {
        return employeeDao.getAllEmployees(leaveManagerSearchCriteria);
    }

    @Override
    @Transactional
    public List<Employee> listEmployeesByManager(Employee manager){
        return employeeDao.listAllEmployeeByManager(manager);
    }

    @Override
    @Transactional
    public List<Employee> listEmployeesByHRManager(Employee manager){
        return employeeDao.listAllEmployeeByHRManager(manager);
    }

    @Override
    @Transactional
    public List<Employee> listEmployeesByFinanceManager(Employee manager){
        return employeeDao.listAllEmployeeByFinanceManager(manager);
    }

    @Override
    @Transactional
    public List<Employee> listEmployeesByTicketManager(Employee manager){
        return employeeDao.listAllEmployeeByTicketManager(manager);
    }

    @Transactional
    public List<Employee> listLeaveManagers() {
        return employeeDao.listLeaveManagers();
    }

    @Transactional
    public List<Employee> listHRLeaveManagers() {
        return employeeDao.listHRLeaveManagers();
    }

    @Transactional
    public List<Employee> listFinanceLeaveManagers() {
        return employeeDao.listFinanceLeaveManagers();
    }

    @Transactional
    public List<Employee> listTicketLeaveManagers() {
        return employeeDao.listTicketLeaveManagers();
    }

    @Transactional
    public int countEmployees() {
        return employeeDao.countAll();
    }

    @Transactional
    public void saveRole(Role role) {
        roleDao.save(role);
    }

    @Transactional
    public void removeRole(Role role) throws ConstraintViolationException {
        roleDao.remove(role);
    }

    @Transactional
    public List<Role> listRoles() {
        return roleDao.listAll();
    }

    @Transactional
    public int countRoles() {
        return roleDao.countAll();
    }

    @Override
    @Transactional
    public Integer getRolesCount(RoleSearchCriteria roleSearchCriteria) {
        return roleDao.getRolesCount(roleSearchCriteria);
    }

    @Override
    @Transactional
    public List<Role> getRolesList(RoleSearchCriteria roleSearchCriteria) {
        return roleDao.getRolesList(roleSearchCriteria);
    }

    @Transactional
    public void saveDepartment(Department department) {
        departmentDao.save(department);
    }

    @Override
    @Transactional
    public Integer getDepartmentCount(DepartmentsSearchCriteira departmentsSearchCriteira) {
        return departmentDao.getDepartmentCount(departmentsSearchCriteira);
    }

    @Override
    @Transactional
    public List<Department> getDepartmentList(DepartmentsSearchCriteira departmentsSearchCriteira) {
        return departmentDao.getDepartmentList(departmentsSearchCriteira);
    }

    @Transactional
    public void removeDepartment(Department department) throws ConstraintViolationException {
        departmentDao.remove(department);
    }

    @Transactional
    public List<Department> listDepartments() {
        return departmentDao.listAll();
    }

    @Transactional
    public List<Department> listDepartmentsByChartId() {
        return departmentDao.listAllOrderByChartId();
    }

    @Transactional
    public int countDepartments() {
        return departmentDao.countAll();
    }

    @Transactional
    public void saveCompany(Company company) {
        companyDao.save(company);
    }

    @Transactional
    public void removeCompany(Company company) throws ConstraintViolationException {
        companyDao.remove(company);
    }

    @Transactional
    public List<Company> listCompanies() {
        return companyDao.listAll();
    }

    @Transactional
    public int countCompanies() {
        return companyDao.countAll();
    }

    @Override
    @Transactional
    public Integer getCompanyCount(CompanySearchCriteria companySearchCriteria) {
        return companyDao.getCompanyCount(companySearchCriteria);
    }

    @Override
    @Transactional
    public List<Company> getCompanyList(CompanySearchCriteria companySearchCriteria) {
        return companyDao.getCompanyList(companySearchCriteria);
    }
}
