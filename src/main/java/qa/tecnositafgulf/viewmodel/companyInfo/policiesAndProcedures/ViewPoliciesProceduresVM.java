package qa.tecnositafgulf.viewmodel.companyInfo.policiesAndProcedures;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.web.servlet.Servlets;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.DesktopUnavailableException;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Window;
import qa.tecnositafgulf.config.MyProperties;
import qa.tecnositafgulf.model.companyInfo.PolicyProcedure;
import qa.tecnositafgulf.service.PolicyProcedureService;
import qa.tecnositafgulf.spring.config.AppConfig;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by ledio on 5/16/18.
 */
public class ViewPoliciesProceduresVM extends IntranetVM {

    private List<PolicyProcedure> policyProcedureModel;
    private Timer timer;
    private boolean viewNewPolicyProcedure;
    private PolicyProcedureService policyProcedureService;
    private Desktop desktop;
    private Integer check;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("detailsPolicyProcedure") PolicyProcedure policyProcedure) {
        init();
        Double ie = Servlets.getBrowser(super.getRequest(), "ie");
        if (ie != null && ie < 8.0) {
            Clients.showNotification("This demo does not support IE6/7", true);
        }
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        policyProcedureService = context.getBean(PolicyProcedureService.class);
        viewNewPolicyProcedure = super.isPublisher();
        loadPolicyProcedures();

        if(policyProcedure != null) {
            for (Iterator<PolicyProcedure> policyProcedureIterator = policyProcedureModel.iterator(); policyProcedureIterator.hasNext(); ) {
                PolicyProcedure policyProcedure1 = policyProcedureIterator.next();
                if (policyProcedure1.equals(policyProcedure)) {
                    this.setCheck(1);
                }
            }
        }
        desktop = Executions.getCurrent().getDesktop();
        if(timer!=null)
            timer.cancel();
        timer = new Timer();
        timer.schedule(updatePosts(),0,1000);
    }

    @Command
    public void newPolicyProcedure(){
        Window window = (Window) Executions.createComponents("/pages/editor/policy-procedure-editor.zul", null, null);
        window.doModal();
    }

    @Command
    public void editPolicyProcedure(@BindingParam("policyProcedure")PolicyProcedure policyProcedure){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("policyProcedure", policyProcedure);
        Window window = (Window) Executions.createComponents("/pages/editor/policy-procedure-editor.zul", null, map);
        window.doModal();

    }

    @Command
    public void deletePolicyProcedure(@BindingParam("policyProcedure")PolicyProcedure policyProcedure){
        policyProcedureService.removePolicyProcedure(policyProcedure);
    }

    public String toString(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE MMMM, dd yyyy");
        return  sdf.format(date);
    }

    public void loadPolicyProcedures(){
        List<PolicyProcedure> policyProcedures = policyProcedureService.listPolicyProcedures();
        policyProcedureModel = policyProcedures;
        BindUtils.postNotifyChange(null, null, this, "policyProcedureModel");
    }

    private int policyProceduresSize(){
        List<PolicyProcedure> policyProcedures = policyProcedureService.listPolicyProcedures();
        return policyProcedures.size();
    }

    private boolean update(){
        if(policyProceduresSize() != policyProcedureModel.size())
            return true;
        return false;
    }

    public String getAuthorImage(PolicyProcedure policyProcedure){
        String ret = "";

        if (policyProcedure.getAuthor() != null && policyProcedure.getAuthor().getImage() != null)
            ret += MyProperties.getInstance().getImagePath() + "/staff/" + policyProcedure.getAuthor().getImage();

        return ret;
    }

    private TimerTask updatePosts() {
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    if(desktop == null) {
                        timer.cancel();
                        return;
                    }
                    Executions.activate(desktop);
                    try {
                        if(update())
                            loadPolicyProcedures();
                    } finally {
                        Executions.deactivate(desktop);
                    }
                }catch(DesktopUnavailableException ex) {
                    System.out.println("Desktop currently unavailable");
                    timer.cancel();
                }catch(InterruptedException e) {
                    System.out.println("The server push thread interrupted");
                    timer.cancel();
                }
            }
        };
    }










    public Integer getCheck() {
        return check;
    }

    public void setCheck(Integer check) {
        this.check = check;
    }

    public List<PolicyProcedure> getPolicyProcedureModel() {
        return policyProcedureModel;
    }

    public void setPolicyProcedureModel(List<PolicyProcedure> policyProcedureModel) {
        this.policyProcedureModel = policyProcedureModel;
    }

    public boolean isViewNewPolicyProcedure() {
        return viewNewPolicyProcedure;
    }

    public void setViewNewPolicyProcedure(boolean viewNewPolicyProcedure) {
        this.viewNewPolicyProcedure = viewNewPolicyProcedure;
    }
}
