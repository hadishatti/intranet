package qa.tecnositafgulf.viewmodel.tender;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.*;
import qa.tecnositafgulf.config.MyProperties;
import qa.tecnositafgulf.model.administration.Employee;
import qa.tecnositafgulf.model.tenders.Tender;
import qa.tecnositafgulf.model.tenders.TenderComment;
import qa.tecnositafgulf.service.TenderCommentService;
import qa.tecnositafgulf.spring.config.AppConfig;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import java.util.*;

/**
 * Created by hadi on 12/24/17.
 */
public class ViewTenderCommentsViewModel  extends IntranetVM {
    private TenderCommentService tenderCommentService;
    private Tender currentTender;
    private List<TenderComment> tenderComments;
    private String currentComment = "";
    private Timer timer;
    private Desktop desktop;


    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Execution exe = Executions.getCurrent();
        Map map = exe.getArg();
        currentTender = (Tender) map.get("tender");
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        tenderCommentService = context.getBean(TenderCommentService.class);
        loadComments();
        desktop = Executions.getCurrent().getDesktop(); //there is no need to activate push since it's already activated
        if(timer!=null)
            timer.cancel();
        timer = new Timer();
        timer.schedule(updateComments(),0,1000);
    }


    private void loadComments(){
        tenderComments = tenderCommentService.listCommentsByTender(currentTender);
        BindUtils.postNotifyChange(null, null, this, "tenderComments");
    }

    private int commentSize(){
        return tenderCommentService.listCommentsByTender(currentTender).size();
    }

    @Command
    @NotifyChange({"currentComment","tenderComments"})
    public void addComment() {
        if(currentComment.isEmpty())
            return;
        Employee employee = (Employee) Sessions.getCurrent().getAttribute("employee");
        TenderComment tenderComment = new TenderComment();
        tenderComment.setAuthor(employee);
        tenderComment.setDate(new Date());
        tenderComment.setContent(currentComment);
        tenderComment.setTender(currentTender);
        tenderCommentService.saveComment(tenderComment);
        currentComment = "";
        loadComments();
    }

    @Command
    public void close(){
        Executions.sendRedirect("/pages/tender/view-tenders.zul");
    }

    public String getAuthorImage(TenderComment tender){
        String ret = "";

        if (tender.getAuthor() != null && tender.getAuthor().getImage() != null)
            ret += MyProperties.getInstance().getImagePath() + "/staff/" + tender.getAuthor().getImage();

        return ret;
    }


    public Tender getCurrentTender() {
        return currentTender;
    }

    public void setCurrentTender(Tender currentTender) {
        this.currentTender = currentTender;
    }

    public List<TenderComment> getTenderComments() {
        return tenderComments;
    }

    public void setTenderComments(List<TenderComment> tenderComments) {
        this.tenderComments = tenderComments;
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
                        if(commentSize()!= tenderComments.size())
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
