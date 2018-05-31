package qa.tecnositafgulf.viewmodel.companyInfo.partners;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.sys.PageCtrl;
import org.zkoss.zul.Messagebox;
import qa.tecnositafgulf.model.companyInfo.Partner;
import qa.tecnositafgulf.service.CompanyInfoService;
import qa.tecnositafgulf.spring.config.AppConfig;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import javax.persistence.PersistenceException;
import java.io.IOException;

/**
 * Created by ameljo on 06/05/18.
 */
public class SavePartnerViewModel extends IntranetVM{

    private CompanyInfoService service;
    private Partner partner;
    private boolean modify = false;
    private boolean currentUser = false;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("partnerToModify") Partner userToModify) {
        init();
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        service = context.getBean(CompanyInfoService.class);
        if(userToModify!=null){
            modify = true;
            partner = userToModify;
            if(partner.equals((Partner) Sessions.getCurrent().getAttribute("partner")))
                currentUser=true;
        }else{
            modify = false;
            partner = new Partner();
        }
        addCommonTags((PageCtrl) view.getPage());
    }

    @Command
    @NotifyChange({"partner"})
    public void savePartner() throws IOException {
        if(!modify) {
            if(partner.getHref().isEmpty()){
                Messagebox.show("Link Cannot be Empty!", "Error", Messagebox.OK, Messagebox.EXCLAMATION);
                return;
            }
            if(partner.getImg().isEmpty()){
                Messagebox.show("Source Cannot be Empty!", "Error", Messagebox.OK, Messagebox.EXCLAMATION);
                return;
            }
        }

        try {
            service.savePartner(partner);
            Messagebox.show("OK! Do you want to view all the Partners?", "Information", Messagebox.OK | Messagebox.CANCEL, Messagebox.INFORMATION, new EventListener<Event>() {
                public void onEvent(Event event) throws Exception {
                    if (event.getName().equals("onOK")) {
                        Executions.sendRedirect("/pages/company-info/partners/partners.zul");
                    }
                }
            });
        }catch (PersistenceException persistenceException){
            Messagebox.show("User already exist with same name", "Information", Messagebox.OK | Messagebox.CANCEL, Messagebox.INFORMATION);
        }
    }

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public boolean isModify() {
        return modify;
    }

    public void setModify(boolean modify) {
        this.modify = modify;
    }

    public boolean isCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(boolean currentUser) {
        this.currentUser = currentUser;
    }

    public CompanyInfoService getService() {
        return service;
    }

    public void setService(CompanyInfoService service) {
        this.service = service;
    }
}
