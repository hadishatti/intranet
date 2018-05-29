package qa.tecnositafgulf.viewmodel.companyEvent;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.*;
import qa.tecnositafgulf.model.administration.Employee;
import qa.tecnositafgulf.model.events.CompanyEvent;
import qa.tecnositafgulf.model.events.EventComment;
import qa.tecnositafgulf.service.EventCommentService;
import qa.tecnositafgulf.spring.config.AppConfig;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import java.util.*;

/**
 * Created by hadi on 12/24/17.
 */
public class ViewEventCommentsViewModel extends IntranetVM{
    private EventCommentService eventCommentService;
    private CompanyEvent currentCompanyEvent;
    private List<EventComment> eventComments;
    private String currentComment = "";
    private Timer timer;
    private Desktop desktop;


    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Execution exe = Executions.getCurrent();
        Map map = exe.getArg();
        currentCompanyEvent = (CompanyEvent) map.get("companyEvent");
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        eventCommentService = context.getBean(EventCommentService.class);
        loadComments();
        desktop = Executions.getCurrent().getDesktop(); //there is no need to activate push since it's already activated
        if(timer!=null)
            timer.cancel();
        timer = new Timer();
        timer.schedule(updateComments(),0,1000);
    }


    private void loadComments(){
        eventComments = eventCommentService.listCommentsByEvent(currentCompanyEvent);
        BindUtils.postNotifyChange(null, null, this, "eventComments");
    }

    private int commentSize(){
        return eventCommentService.listCommentsByEvent(currentCompanyEvent).size();
    }

    @Command
    @NotifyChange({"currentComment","eventComments"})
    public void addComment() {
        if(currentComment.isEmpty())
            return;
        Employee employee = (Employee) Sessions.getCurrent().getAttribute("employee");
        EventComment eventComment = new EventComment();
        eventComment.setAuthor(employee);
        eventComment.setDate(new Date());
        eventComment.setContent(currentComment);
        eventComment.setCompanyEvent(currentCompanyEvent);
        eventCommentService.saveComment(eventComment);
        currentComment = "";
        loadComments();
    }

    @Command
    public void close(){
        Executions.sendRedirect("/pages/companyEvent/view-companyEvents.zul");
    }


    public CompanyEvent getCurrentCompanyEvent() {
        return currentCompanyEvent;
    }

    public void setCurrentCompanyEvent(CompanyEvent currentCompanyEvent) {
        this.currentCompanyEvent = currentCompanyEvent;
    }

    public List<EventComment> getEventComments() {
        return eventComments;
    }

    public void setEventComments(List<EventComment> eventComments) {
        this.eventComments = eventComments;
    }

    public String getCurrentComment() {
        return currentComment;
    }

    public void setCurrentComment(String currentComment) {
        this.currentComment = currentComment;
    }

    private TimerTask updateComments() {
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
                        if(commentSize()!= eventComments.size())
                            loadComments();
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
}
