package qa.tecnositafgulf.viewmodel.helpDesk;

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
import qa.tecnositafgulf.model.helpDesk.HelpDesk;
import qa.tecnositafgulf.searchcriteria.helpDesk.HelpDeskSearchCriteria;
import qa.tecnositafgulf.service.HelpDeskService;
import qa.tecnositafgulf.spring.config.AppConfig;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import java.util.*;

/**
 * Created by klajdi on 20/05/18.
 */
public class HelpDeskViewModel extends IntranetVM {

    private List<HelpDesk> helpDeskModel;
    private Timer timer;
    private boolean viewNewHelpDesk;
    private HelpDeskService helpDeskService;
    private Desktop desktop;
    private HelpDeskSearchCriteria helpDeskSearchCriteria;
    private boolean filtered = false;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        init();
        Double ie = Servlets.getBrowser(super.getRequest(), "ie");
        if (ie != null && ie < 8.0) {
            Clients.showNotification("This demo does not support IE6/7", true);
        }
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        helpDeskService = context.getBean(HelpDeskService.class);
        viewNewHelpDesk = super.isPublisher();
        helpDeskSearchCriteria = new HelpDeskSearchCriteria();
        loadHelpDesks();
        desktop = Executions.getCurrent().getDesktop();
        if(timer!=null)
            timer.cancel();
        timer = new Timer();
        timer.schedule(updatePosts(),0,1000);
    }

    public void loadHelpDesks(){
        List<HelpDesk> helpDesks = helpDeskService.listHelpDesks();
        helpDeskModel = helpDesks;
        BindUtils.postNotifyChange(null, null, this, "helpDeskModel");
    }

    public void loadFilteredHelpDesks(){
        List<HelpDesk> helpDesks = helpDeskService.listHelpDesks(helpDeskSearchCriteria);
        helpDeskModel = helpDesks;
        BindUtils.postNotifyChange(null, null, this, "helpDeskModel");
    }

    @Command
    @NotifyChange({"helpDeskSearchCriteria", "helpDeskModel"})
    public void clearFilters() {
        this.helpDeskSearchCriteria.clear();
        this.helpDeskSearchCriteria.clearFilters();
        filtered = false;
        loadHelpDesks();
    }

    @Command
    @NotifyChange({"helpDeskSearchCriteria", "helpDeskModel"})
    public void filter() {
        this.helpDeskSearchCriteria.clear();
        filtered = true;
        loadFilteredHelpDesks();
    }

    private int helpDeskSize(){
        if(!filtered) {
            List<HelpDesk> helpDesks = helpDeskService.listHelpDesks();
            return helpDesks.size();
        }else {
            List<HelpDesk> helpDesks = helpDeskService.listHelpDesks(helpDeskSearchCriteria);
            return helpDesks.size();
        }
    }

    private boolean update(){
        if(helpDeskSize()!= helpDeskModel.size())
            return true;
        return false;
    }

    public void helpDesksFiltered(){
        if(!filtered)
            loadHelpDesks();
        else
            loadFilteredHelpDesks();
    }

    @Command
    public void newHelpDesk(){
        Window window = (Window) Executions.createComponents("/pages/editor/helpDesk-editor.zul", null, null);
        window.doModal();
    }

    @Command
    public void editHelpDesk(@BindingParam("helpDesk")HelpDesk helpDesk){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("helpDesk", helpDesk);
        Window window = (Window) Executions.createComponents("/pages/editor/helpDesk-editor.zul", null, map);
        window.doModal();
    }

    @Command
    public void deleteHelpDesk(@BindingParam("helpDesk")HelpDesk helpDesk){
        helpDeskService.removeHelpDesk(helpDesk);
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
                            helpDesksFiltered();
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

    public List<HelpDesk> getHelpDeskModel() {
        return helpDeskModel;
    }

    public void setHelpDeskModel(List<HelpDesk> helpDeskModel) {
        this.helpDeskModel = helpDeskModel;
    }

    public boolean isViewNewHelpDesk() {
        return viewNewHelpDesk;
    }

    public void setViewNewHelpDesk(boolean viewNewHelpDesk) {
        this.viewNewHelpDesk = viewNewHelpDesk;
    }

    public HelpDeskSearchCriteria getHelpDeskSearchCriteria() {
        return helpDeskSearchCriteria;
    }

    public void setHelpDeskSearchCriteria(HelpDeskSearchCriteria helpDeskSearchCriteria) {
        this.helpDeskSearchCriteria = helpDeskSearchCriteria;
    }

    public boolean isFiltered() {
        return filtered;
    }

    public void setFiltered(boolean filtered) {
        this.filtered = filtered;
    }
}
