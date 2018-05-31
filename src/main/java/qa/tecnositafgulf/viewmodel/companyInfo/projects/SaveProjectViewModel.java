package qa.tecnositafgulf.viewmodel.companyInfo.projects;

import org.apache.xmlbeans.impl.util.Base64;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.annotation.*;
import org.zkoss.image.AImage;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.sys.PageCtrl;
import org.zkoss.zul.Image;
import org.zkoss.zul.Messagebox;
import qa.tecnositafgulf.config.MyProperties;
import qa.tecnositafgulf.config.ProjectsStatusEnum;
import qa.tecnositafgulf.model.administration.Employee;
import qa.tecnositafgulf.model.companyInfo.Project;
import qa.tecnositafgulf.service.AdministrationService;
import qa.tecnositafgulf.service.CompanyInfoService;
import qa.tecnositafgulf.spring.config.AppConfig;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import javax.persistence.PersistenceException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by ameljo on 5/19/18.
 */
public class SaveProjectViewModel extends IntranetVM {
    @Wire
    private Image image;

    private CompanyInfoService service;
    private AdministrationService administrationService;
    private Project project;
    private boolean uploaded = false;
    private String link = "";
    private boolean modify = false;
    private boolean currentUser = false;
    private ProjectsStatusEnum statusEnum;
    private List<Employee> employeeList;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("projectToModify") Project projectToModify) {
        init();
        Selectors.wireComponents(view, this, false);
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        service = context.getBean(CompanyInfoService.class);
        administrationService = context.getBean(AdministrationService.class);
        if(projectToModify!=null){
            modify = true;
            project = projectToModify;
            if(project.equals((Project) Sessions.getCurrent().getAttribute("project")))
                currentUser=true;
            image.setSrc(project.getImage());
            if(!project.getImage().contains("data:image")) {
                link = project.getImage();
                if (link.equals("NONE"))
                    image.setSrc(MyProperties.getInstance().getImagePath() + "/" + link + ".png");
            }
        }else{
            modify = false;
            project = new Project();
        }

        employeeList = administrationService.listEmployees();
        addCommonTags((PageCtrl) view.getPage());
    }

    @Command
    @NotifyChange({"image"})
    public void link() {
        image.setSrc(link);
        uploaded=false;
    }

    @Command
    @NotifyChange({"image"})
    public void upload(@ContextParam(ContextType.BIND_CONTEXT) BindContext ctx) {
        UploadEvent upEvent = null;
        Object objUploadEvent = ctx.getTriggerEvent();
        if (objUploadEvent != null && (objUploadEvent instanceof UploadEvent)) {
            upEvent = (UploadEvent) objUploadEvent;
        }
        if (upEvent != null) {
            Media media = upEvent.getMedia();
            int lengthofImage = media.getByteData().length;
            if (media instanceof  org.zkoss.image.Image) {
                if (lengthofImage > 500 * 1024) {
                    showInfo("Please Select a Image of size less than 500Kb.");
                    return;
                }
                else{
                    image.setContent((AImage) media);//Initialize the bind object to show image in zul page and Notify it also
                    uploaded=true;
                }
            }
            else {
                showInfo("The Selected File is not an Image.");
            }
        } else {
            showInfo("Upload Failed.");
        }
    }

    protected void showInfo(String message) {
        Messagebox.show(message, "Error!", Messagebox.OK, Messagebox.INFORMATION);
    }

    @Command
    public void saveProject(){
        if(!modify) {
            if(project.getName().isEmpty()){
                Messagebox.show("Name Cannot be Empty!", "Error", Messagebox.OK, Messagebox.EXCLAMATION);
                return;
            }
        }

        try {
            if(uploaded)
                project.setImage("data:image/png;base64,"+new String(Base64.encode(image.getContent().getByteData()),"UTF-8"));
            else {
                if (image.getSrc() != null && !image.getSrc().isEmpty() && !image.getSrc().equals(MyProperties.getInstance().getImagePath() + "/NONE.png"))
                    project.setImage(image.getSrc());
                else
                    project.setImage("NONE");
            }
        } catch (UnsupportedEncodingException e) {
            showInfo("Error during saving the image!");
        }

        try {
            service.saveProject(project);
            Messagebox.show("OK! Do you want to view all the projects?", "Information", Messagebox.OK | Messagebox.CANCEL, Messagebox.INFORMATION, new EventListener<Event>() {
                public void onEvent(Event event) throws Exception {
                    if (event.getName().equals("onOK")) {
                        Executions.sendRedirect("/pages/company-info/projects/projects.zul");
                    }
                }
            });
        }catch (PersistenceException persistenceException){
            Messagebox.show("User already exist with same name", "Information", Messagebox.OK | Messagebox.CANCEL, Messagebox.INFORMATION);
        }
    }

    public Project getproject() {
        return project;
    }

    public void setproject(Project project) {
        this.project = project;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public ProjectsStatusEnum getStatusEnum() {
        return statusEnum;
    }

    public void setStatusEnum(ProjectsStatusEnum statusEnum) {
        this.statusEnum = statusEnum;
    }

    public List<Employee> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(List<Employee> employeeList) {
        this.employeeList = employeeList;
    }
}
