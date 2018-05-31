package qa.tecnositafgulf.viewmodel.administration.roles;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.sys.PageCtrl;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;
import qa.tecnositafgulf.model.administration.Role;
import qa.tecnositafgulf.searchcriteria.RoleSearchCriteria;
import qa.tecnositafgulf.service.AdministrationService;
import qa.tecnositafgulf.spring.config.AppConfig;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hadi on 12/17/17.
 */
public class ViewRolesViewModel extends IntranetVM{

    private AdministrationService service;
    private RoleSearchCriteria roleSearchCriteria;
    private List<Role> roles;
    private Integer totalSize;
    private Integer activePage;

    @AfterCompose
    public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view){
        init();
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        service = context.getBean(AdministrationService.class);
        roleSearchCriteria = new RoleSearchCriteria();
        this.setActivePage(this.roleSearchCriteria.getStartIndex());
        loadData();
        addCommonTags((PageCtrl) view.getPage());
    }

    private void loadData(){
        this.setTotalSize(this.service.getRolesCount(roleSearchCriteria));
        this.roleSearchCriteria.setStartIndex(getActivePage());
        this.roles = this.service.getRolesList(roleSearchCriteria);
    }

    @Command
    @NotifyChange({"activePage", "totalSize", "roleSearchCriteria", "roles"})
    public void order(@BindingParam("orderBy") String orderBy){
        this.roleSearchCriteria.setOrderByFieldName(orderBy);
        this.roleSearchCriteria.setPageOrderMode("asc".equalsIgnoreCase(this.roleSearchCriteria.getPageOrderMode()) ? "desc" : "asc");
        //load method call to load data
        loadData();
    }

    @Command
    @NotifyChange({"activePage", "totalSize", "roleSearchCriteria", "roles"})
    public void clearFilters() {
        this.roleSearchCriteria.clear();
        this.roleSearchCriteria.setPageOrderMode("desc");
        this.roleSearchCriteria.clearFilters();
        loadData();
    }

    @Command
    @NotifyChange({"activePage", "totalSize", "roleSearchCriteria", "roles"})
    public void filter() {
        this.roleSearchCriteria.clear();
        this.roleSearchCriteria.setPageOrderMode("desc");
        this.activePage = this.roleSearchCriteria.getStartIndex();
        loadData();
    }


    @NotifyChange("roles")
    private void load(){
        loadData();
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @Command
    @NotifyChange("roles")
    public void modify(@BindingParam("item") Role role){
        final Map<String, Role> params = new HashMap<String, Role>();
        params.put("roleToModify", role);
        ((Window) Executions.getCurrent().createComponents("/pages/administration/roles/saveRole.zul", null, params)).doModal();
    }

    @Command
    @NotifyChange("roles")
    public void delete(@BindingParam("item") final Role role){
        try {
            Messagebox.show("Do you want to delete this Role?", "Warning", Messagebox.OK | Messagebox.CANCEL, Messagebox.INFORMATION, new EventListener<Event>() {
                public void onEvent(Event event) throws Exception {
                    if (event.getName().equals("onOK")) {
                        roles.remove(role);
                        service.removeRole(role);
                        loadData();
                        Executions.sendRedirect("/pages/administration/roles/viewRoles.zul");
                    }
                }
            });
        }catch(ConstraintViolationException e){/** @Todo FIX **/
            Clients.showNotification("The Role is used by an Employee.\nYou Cannot Delete it unless you delete first the Employee!", true);
            return;
        }
    }

    @Command
    @NotifyChange("roles")
    public void add(){
        final Map<String, Role> params = new HashMap<String, Role>();
        params.put("roleToModify", null);
        ((Window) Executions.getCurrent().createComponents("/pages/administration/roles/saveRole.zul", null, params)).doModal();
    }

    public AdministrationService getService() {
        return service;
    }

    public void setService(AdministrationService service) {
        this.service = service;
    }

    public RoleSearchCriteria getRoleSearchCriteria() {
        return roleSearchCriteria;
    }

    public void setRoleSearchCriteria(RoleSearchCriteria roleSearchCriteria) {
        this.roleSearchCriteria = roleSearchCriteria;
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

    @NotifyChange({"activePage", "totalSize", "roleSearchCriteria", "roles"})
    public void setActivePage(Integer activePage) {
        this.activePage = activePage;
        loadData();
    }
}
