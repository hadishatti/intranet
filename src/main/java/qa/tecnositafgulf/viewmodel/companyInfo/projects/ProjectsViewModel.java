package qa.tecnositafgulf.viewmodel.companyInfo.projects;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.sys.PageCtrl;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;
import qa.tecnositafgulf.config.MyProperties;
import qa.tecnositafgulf.model.administration.Employee;
import qa.tecnositafgulf.model.companyInfo.Project;
import qa.tecnositafgulf.searchcriteria.ProjectSearchCriteria;
import qa.tecnositafgulf.service.AdministrationService;
import qa.tecnositafgulf.service.CompanyInfoService;
import qa.tecnositafgulf.spring.config.AppConfig;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ameljo on 5/16/18.
 */
public class ProjectsViewModel extends IntranetVM {

    private List<Project> projects;
    private CompanyInfoService service;
    private ProjectSearchCriteria searchCriteria;
    private Integer activePage;
    private Integer totalSize;
    private AdministrationService administrationService;
    private List<Employee> employeeList;



    @Init
    public void Init(){
        super.init();
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        administrationService = context.getBean(AdministrationService.class);
        service = context.getBean(CompanyInfoService.class);
        searchCriteria = new ProjectSearchCriteria();
        this.setActivePage(this.searchCriteria.getStartIndex());
        employeeList = administrationService.listEmployees();
        loadData();
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view){
        addCommonTags((PageCtrl) view.getPage());
    }

    @Command
    @NotifyChange("projects")
    public void add(){
        final Map<String, Project> params = new HashMap<String, Project>();
        params.put("projectToModify", null);
        ((Window) Executions.getCurrent().createComponents("/pages/company-info/projects/saveProject.zul", null, params)).doModal();
    }

    @Command
    @NotifyChange("project")
    public void modify(@BindingParam("item") Project project){
        final Map<String, Project> params = new HashMap<String, Project>();
        params.put("projectToModify", project);
        ((Window) Executions.getCurrent().createComponents("/pages/company-info/projects/saveProject.zul", null, params)).doModal();
    }

    @Command
    @NotifyChange("project")
    public void delete(@BindingParam("item") final Project project){
        Messagebox.show("Do you want to delete this project?", "Warning", Messagebox.OK | Messagebox.CANCEL, Messagebox.INFORMATION, new EventListener<Event>() {
            public void onEvent(Event event) throws Exception {
                if (event.getName().equals("onOK")) {
                    projects.remove(project);
                    service.removeProject(project);
                    Executions.sendRedirect("/pages/company-info/projects/projects.zul");
                }
            }
        });
    }

    @Command
    @NotifyChange({"activePage", "totalSize", "searchCriteria", "projects"})
    public void clearFilters() {
        this.searchCriteria.clear();
        this.searchCriteria.setPageOrderMode("desc");
        this.searchCriteria.clearFilters();
        this.loadData();
    }

    @Command
    @NotifyChange({"activePage", "totalSize", "searchCriteria", "projects"})
    public void filter() {
        this.searchCriteria.clear();
        this.searchCriteria.setPageOrderMode("desc");
        this.activePage = this.searchCriteria.getStartIndex();
        this.loadData();
    }

    public void loadData(){
        this.setTotalSize(this.service.getProjectsCount(searchCriteria));
        this.searchCriteria.setStartIndex(getActivePage());
        this.projects = this.service.getProjectsList(searchCriteria);
    }

    public String getImage(Project project){
        if(project.getImage().equals("NONE"))
            return  MyProperties.getInstance().getImagePath() + "/NONE.png";
        else
            return project.getImage();
    }

    public String fullText(Project project) {
        return "'" + project.getContent() + "'";
    }

    public String shortText(Project project){
        if(project.getContent().length() > 600)
            return "'" + project.getContent().substring(0, 597).concat("...") + "'";
        else
            return "'" + project.getContent() + "'";
    }

    public String getLabelText(Project project){
        if(project.getContent().length() > 600)
            return project.getContent().substring(0, 597).concat("...");
        else
            return project.getContent();
    }

    public boolean textExtentsLimit(Project project){
        return  project.getContent().length() > 600;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public Integer getActivePage() {
        return activePage;
    }

    public void setActivePage(Integer activePage) {
        this.activePage = activePage;
    }

    public Integer getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Integer totalSize) {
        this.totalSize = totalSize;
    }

    public ProjectSearchCriteria getSearchCriteria() {
        return searchCriteria;
    }

    public void setSearchCriteria(ProjectSearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    public List<Employee> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(List<Employee> employeeList) {
        this.employeeList = employeeList;
    }
}
