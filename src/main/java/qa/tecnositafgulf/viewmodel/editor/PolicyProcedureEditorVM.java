package qa.tecnositafgulf.viewmodel.editor;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.sys.PageCtrl;
import org.zkoss.zul.Messagebox;
import qa.tecnositafgulf.config.IntranetProperties;
import qa.tecnositafgulf.model.administration.Employee;
import qa.tecnositafgulf.model.companyInfo.PolicyProcedure;
import qa.tecnositafgulf.service.PolicyProcedureService;
import qa.tecnositafgulf.spring.config.AppConfig;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by ledio on 5/16/18.
 */
public class PolicyProcedureEditorVM extends IntranetVM{

    private PolicyProcedureService policyProcedureService;
    private String content = "";
    private PolicyProcedure policyProcedureToEdit=null;
    private String title="";
    private List<String> types;
    private String selectedType;
    private String procedureId="";
    private String number="";

    @AfterCompose
    public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view){
        init();
        Selectors.wireComponents(view, this, false);
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        policyProcedureService = context.getBean(PolicyProcedureService.class);
        Map map = Executions.getCurrent().getArg();
        policyProcedureToEdit = (PolicyProcedure) map.get("policyProcedure");
        types = IntranetProperties.PolicyProcedureType.getAll();
        selectedType = IntranetProperties.PolicyProcedureType.Policy.toString();
        if(policyProcedureToEdit!=null) {
            title = policyProcedureToEdit.getTitle();
            content = policyProcedureToEdit.getContent();
            selectedType = policyProcedureToEdit.getType();
            number = policyProcedureToEdit.getNumber();
            if (isProcedure())
                procedureId = policyProcedureToEdit.getProcedureId();
            else procedureId = null;
        }
        addCommonTags((PageCtrl) view.getPage());
    }

    protected void showInfo(String message) {
        Messagebox.show(message, "Error!", Messagebox.OK, Messagebox.INFORMATION);
    }

    @Command
    public void post(@BindingParam("content") String content){
        this.content = content;

        PolicyProcedure policyProcedure;
        if(policyProcedureToEdit!=null)
            policyProcedure = policyProcedureToEdit;
        else
            policyProcedure = new PolicyProcedure();
        policyProcedure.setTitle(title);
        policyProcedure.setType(selectedType);
        policyProcedure.setNumber(number);
        policyProcedure.setProcedureId(procedureId);
        policyProcedure.setContent(content);
        policyProcedure.setDate(new Date());
        policyProcedure.setAuthor(((Employee) Sessions.getCurrent().getAttribute("employee")));
        try {
            policyProcedureService.savePolicyProcedure(policyProcedure);
        }catch (Exception e){
            Messagebox.show("An Error Occured! Please contact the System Administrator.","Error", Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }
        Messagebox.show(selectedType + " Posted!", "Information", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
            public void onEvent(org.zkoss.zk.ui.event.Event event) throws Exception {
                Executions.sendRedirect("/pages/company-info/policies/policies-procedures.zul");
            }
        });
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public String getSelectedType() {
        return selectedType;
    }

    public void setSelectedType(String selectedType) {
        this.selectedType = selectedType;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getProcedureId() {
        return procedureId;
    }

    public void setProcedureId(String procedureId) {
        this.procedureId = procedureId;
    }

    public boolean isProcedure() {
        if (selectedType.equals(IntranetProperties.PolicyProcedureType.Procedure.toString()))
            return true;
        else {
            procedureId = "";
            return false;
        }
    }
}
