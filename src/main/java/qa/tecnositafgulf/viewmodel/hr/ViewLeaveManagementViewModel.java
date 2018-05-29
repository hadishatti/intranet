package qa.tecnositafgulf.viewmodel.hr;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Window;
import qa.tecnositafgulf.model.administration.Employee;
import qa.tecnositafgulf.searchcriteria.LeaveManagerSearchCriteria;
import qa.tecnositafgulf.service.AdministrationService;
import qa.tecnositafgulf.spring.config.AppConfig;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hadi on 12/31/17.
 */
public class ViewLeaveManagementViewModel extends IntranetVM {
    private AdministrationService service;
    private LeaveManagerSearchCriteria leaveManagerSearchCriteria;
    private List<Employee> employees;
    private Integer totalSize;
    private Integer activePage;

    @AfterCompose
    public void doAfterCompose(){
        init();
        if(!super.isHRLeaveManager() && !super.isAdministrator()){
            Executions.sendRedirect("/pages/home.zul");
        }
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        service = context.getBean(AdministrationService.class);
        leaveManagerSearchCriteria = new LeaveManagerSearchCriteria();
        this.setActivePage(this.leaveManagerSearchCriteria.getStartIndex());
        loadData();
    }

    public boolean isLeaveManager(Employee employee){
        return authenticationService.isProfilePresent(employee, "LM");
    }

    public boolean isHRLeaveManager(Employee employee){
        return authenticationService.isProfilePresent(employee, "HRLM");
    }

    public boolean isFinanceLeaveManager(Employee employee){
        return authenticationService.isProfilePresent(employee, "FLM");
    }

    public boolean isTicketLeaveManager(Employee employee){
        return authenticationService.isProfilePresent(employee, "TLM");
    }

    private void loadData(){
        Integer count = this.service.getEmployeesCount(leaveManagerSearchCriteria);
        this.setTotalSize(count);
        this.leaveManagerSearchCriteria.setStartIndex(getActivePage());
        this.employees = this.service.getAllEmployees(leaveManagerSearchCriteria);
        System.out.println("count of employee records = "+count);
        System.out.println("employee list = "+employees.toString());

    }

    @Command
    @NotifyChange({"activePage", "totalSize", "leaveManagerSearchCriteria", "employees"})
    public void order(@BindingParam("orderBy") String orderBy){
        this.leaveManagerSearchCriteria.setOrderByFieldName(orderBy);
        this.leaveManagerSearchCriteria.setPageOrderMode("asc".equalsIgnoreCase(this.leaveManagerSearchCriteria.getPageOrderMode()) ? "desc" : "asc");
        //load method call to load data
        loadData();
    }

    @Command
    @NotifyChange({"activePage", "totalSize", "leaveManagerSearchCriteria", "employees"})
    public void clearFilters() {
        this.leaveManagerSearchCriteria.clear();
        this.leaveManagerSearchCriteria.setPageOrderMode("desc");
        this.leaveManagerSearchCriteria.clearFilters();
        loadData();
    }

    @Command
    @NotifyChange({"activePage", "totalSize", "leaveManagerSearchCriteria", "employees"})
    public void filter() {
        this.leaveManagerSearchCriteria.clear();
        this.leaveManagerSearchCriteria.setPageOrderMode("desc");
        this.activePage = this.leaveManagerSearchCriteria.getStartIndex();
        loadData();
    }

    @NotifyChange("employees")
    private void load(){
        loadData();
    }



    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    @Command
    @NotifyChange("employees")
    public void modify(@BindingParam("item") Employee employee){
        final Map<String, Employee> params = new HashMap<String, Employee>();
        params.put("employeeToModify", employee);
        ((Window) Executions.getCurrent().createComponents("/pages/hr/saveLeaveManagement.zul", null, params)).doModal();
    }

    public AdministrationService getService() {
        return service;
    }

    public void setService(AdministrationService service) {
        this.service = service;
    }

    public LeaveManagerSearchCriteria getLeaveManagerSearchCriteria() {
        return leaveManagerSearchCriteria;
    }

    public void setLeaveManagerSearchCriteria(LeaveManagerSearchCriteria leaveManagerSearchCriteria) {
        this.leaveManagerSearchCriteria = leaveManagerSearchCriteria;
    }

    public Integer getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Integer totalSize) {
        this.totalSize = totalSize;
    }

    public Integer getActivePage() {
        return activePage;
    }
    @NotifyChange({"activePage", "totalSize", "leaveManagerSearchCriteria", "employees"})
    public void setActivePage(Integer activePage) {
        this.activePage = activePage;
        loadData();
    }
}
