package qa.tecnositafgulf.viewmodel.administration.employees;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.sys.PageCtrl;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;
import qa.tecnositafgulf.model.administration.Employee;
import qa.tecnositafgulf.searchcriteria.EmployeeSearchCriteria;
import qa.tecnositafgulf.service.AdministrationService;
import qa.tecnositafgulf.spring.config.AppConfig;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hadi on 12/17/17.
 */
public class ViewEmployeesViewModel extends IntranetVM{
    private AdministrationService service;
    private List<Employee> employees;
    private EmployeeSearchCriteria employeeSearchCriteria;
    private Integer totalSize;
    private Integer activePage;

    @AfterCompose
    public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view){
        init();
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        service = context.getBean(AdministrationService.class);
        employeeSearchCriteria = new EmployeeSearchCriteria();
        this.setActivePage(this.employeeSearchCriteria.getStartIndex());
        loadData();
        addCommonTags((PageCtrl) view.getPage());
    }

    private void loadData(){
        this.setTotalSize(this.service.getEmployeeCount(employeeSearchCriteria));
        this.employeeSearchCriteria.setStartIndex(getActivePage());
        this.employees = this.service.getEmployeeList(employeeSearchCriteria).isEmpty()?removeElements():this.service.getEmployeeList(employeeSearchCriteria);
    }

    private List<Employee> removeElements(){
        employees.removeAll(employees);
        return employees;
    }

    @Command
    @NotifyChange({"activePage", "totalSize", "employeeSearchCriteria", "employees"})
    public void order(@BindingParam("orderBy") String orderBy){
        this.employeeSearchCriteria.setOrderByFieldName(orderBy);
        this.employeeSearchCriteria.setPageOrderMode("asc".equalsIgnoreCase(this.employeeSearchCriteria.getPageOrderMode()) ? "desc" : "asc");
        //load method call to load data
        loadData();
    }

    @Command
    @NotifyChange({"activePage", "totalSize", "employeeSearchCriteria", "employees"})
    public void clearFilters() {
        this.employeeSearchCriteria.clear();
        this.employeeSearchCriteria.setPageOrderMode("desc");
        this.employeeSearchCriteria.clearFilters();
        loadData();
    }

    @Command
    @NotifyChange({"activePage", "totalSize", "employeeSearchCriteria", "employees"})
    public void filter() {
        this.employeeSearchCriteria.clear();
        this.employeeSearchCriteria.setPageOrderMode("desc");
        this.activePage = this.employeeSearchCriteria.getStartIndex();
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
        ((Window) Executions.getCurrent().createComponents("/pages/administration/employees/saveEmployee.zul", null, params)).doModal();
    }

    @Command
    @NotifyChange("employees")
    public void delete(@BindingParam("item") final Employee employee){
            Messagebox.show("Do you want to delete this Employee?", "Warning", Messagebox.OK | Messagebox.CANCEL, Messagebox.INFORMATION, new EventListener<Event>() {
                public void onEvent(Event event) throws Exception {
                    if (event.getName().equals("onOK")) {
                        employees.remove(employee);
                        service.removeEmployee(employee);
                        loadData();
                        Executions.sendRedirect("/pages/administration/employees/viewEmployees.zul");
                    }
                }
            });
    }

    @Command
    @NotifyChange("employees")
    public void add(){
        final Map<String, Employee> params = new HashMap<String, Employee>();
        params.put("employeeToModify", null);
        ((Window) Executions.getCurrent().createComponents("/pages/administration/employees/saveEmployee.zul", null, params)).doModal();
    }

    public AdministrationService getService() {
        return service;
    }

    public void setService(AdministrationService service) {
        this.service = service;
    }

    public EmployeeSearchCriteria getEmployeeSearchCriteria() {
        return employeeSearchCriteria;
    }

    public void setEmployeeSearchCriteria(EmployeeSearchCriteria employeeSearchCriteria) {
        this.employeeSearchCriteria = employeeSearchCriteria;
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
    @NotifyChange({"activePage", "totalSize", "employeeSearchCriteria", "employees"})
    public void setActivePage(Integer activePage) {
        this.activePage = activePage;
        loadData();
    }
}
