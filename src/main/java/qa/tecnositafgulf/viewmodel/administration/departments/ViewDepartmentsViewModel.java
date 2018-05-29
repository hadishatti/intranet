package qa.tecnositafgulf.viewmodel.administration.departments;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;
import qa.tecnositafgulf.model.administration.Department;
import qa.tecnositafgulf.searchcriteria.DepartmentsSearchCriteira;
import qa.tecnositafgulf.service.AdministrationService;
import qa.tecnositafgulf.spring.config.AppConfig;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hadi on 12/17/17.
 */
public class ViewDepartmentsViewModel extends IntranetVM{

    private AdministrationService service;
    private List<Department> departments;
    private DepartmentsSearchCriteira departmentsSearchCriteira;
    private Integer totalSize;
    private Integer activePage;


    @AfterCompose
    public void doAfterCompose(){
        init();
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        service = context.getBean(AdministrationService.class);
        departmentsSearchCriteira = new DepartmentsSearchCriteira();
        this.setActivePage(this.departmentsSearchCriteira.getStartIndex());
        loadData();
    }


    private void loadData(){
        this.setTotalSize(this.service.getDepartmentCount(departmentsSearchCriteira));
        this.departmentsSearchCriteira.setStartIndex(getActivePage());
        this.departments = this.service.getDepartmentList(departmentsSearchCriteira);
    }

    @Command
    @NotifyChange({"activePage", "totalSize", "departmentsSearchCriteira", "departments"})
    public void order(@BindingParam("orderBy") String orderBy){
        this.departmentsSearchCriteira.setOrderByFieldName(orderBy);
        this.departmentsSearchCriteira.setPageOrderMode("asc".equalsIgnoreCase(this.departmentsSearchCriteira.getPageOrderMode()) ? "desc" : "asc");
        //load method call to load data
        loadData();
    }

    @Command
    @NotifyChange({"activePage", "totalSize", "departmentsSearchCriteira", "departments"})
    public void clearFilters() {
        this.departmentsSearchCriteira.clear();
        this.departmentsSearchCriteira.setPageOrderMode("desc");
        this.departmentsSearchCriteira.clearFilters();
        loadData();
    }

    @Command
    @NotifyChange({"activePage", "totalSize", "departmentsSearchCriteira", "departments"})
    public void filter() {
        this.departmentsSearchCriteira.clear();
        this.departmentsSearchCriteira.setPageOrderMode("desc");
        this.activePage = this.departmentsSearchCriteira.getStartIndex();
        loadData();
    }



    @NotifyChange("departments")
    private void load(){

        loadData();
    }

    public List<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }

    @Command
    @NotifyChange("departments")
    public void modify(@BindingParam("item") Department department){
        final Map<String, Department> params = new HashMap<String, Department>();
        params.put("departmentToModify", department);
        ((Window) Executions.getCurrent().createComponents("/pages/administration/departments/saveDepartment.zul", null, params)).doModal();
    }

    @Command
    @NotifyChange("departments")
    public void delete(@BindingParam("item") final Department department){
        try {
            Messagebox.show("Do you want to delete this Department?", "Warning", Messagebox.OK | Messagebox.CANCEL, Messagebox.INFORMATION, new EventListener<Event>() {
                public void onEvent(Event event) throws Exception {
                    if (event.getName().equals("onOK")) {
                        departments.remove(department);
                        service.removeDepartment(department);
                        loadData();
                        Executions.sendRedirect("/pages/administration/departments/viewDepartments.zul");
                    }
                }
            });
        }catch(ConstraintViolationException e){/** @Todo FIX **/
            Clients.showNotification("The Department is used by an Employee.\nYou Cannot Delete it unless you delete first the Employee!", true);
            return;
        }
    }

    @Command
    @NotifyChange("departments")
    public void add(){
        final Map<String, Department> params = new HashMap<String, Department>();
        params.put("departmentToModify", null);
        ((Window) Executions.getCurrent().createComponents("/pages/administration/departments/saveDepartment.zul", null, params)).doModal();
    }

    public AdministrationService getService() {
        return service;
    }

    public void setService(AdministrationService service) {
        this.service = service;
    }

    public DepartmentsSearchCriteira getDepartmentsSearchCriteira() {
        return departmentsSearchCriteira;
    }

    public void setDepartmentsSearchCriteira(DepartmentsSearchCriteira departmentsSearchCriteira) {
        this.departmentsSearchCriteira = departmentsSearchCriteira;
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

    @NotifyChange({"activePage", "totalSize", "departmentsSearchCriteira", "departments"})
    public void setActivePage(Integer activePage) {
        this.activePage = activePage;
        loadData();
    }
}
