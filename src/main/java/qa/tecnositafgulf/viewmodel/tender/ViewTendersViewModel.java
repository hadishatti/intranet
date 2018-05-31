package qa.tecnositafgulf.viewmodel.tender;

import java.text.SimpleDateFormat;
import java.util.*;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.DesktopUnavailableException;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.sys.PageCtrl;
import qa.tecnositafgulf.config.MyProperties;
import qa.tecnositafgulf.searchcriteria.tender.TenderSearchCriteria;
import qa.tecnositafgulf.viewmodel.IntranetVM;
import qa.tecnositafgulf.model.tenders.TenderComment;
import qa.tecnositafgulf.model.tenders.Tender;
import qa.tecnositafgulf.service.TenderCommentService;
import qa.tecnositafgulf.service.TenderService;
import qa.tecnositafgulf.spring.config.AppConfig;
import org.zkoss.web.servlet.Servlets;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Window;


public class ViewTendersViewModel extends IntranetVM{

    private List<Tender> tenderModel;
    private Timer timer;
    private boolean viewNewTender;
    private TenderService tenderService;
    private TenderCommentService tenderCommentService;
    private Desktop desktop;
    private Map<Tender,List<TenderComment>> commentList;
    private Map<Tender, Integer> commentNumbers;
    private TenderSearchCriteria tenderSearchCriteria;
    private Integer totalSize;
    private Integer activePage;


    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        init();
        Double ie = Servlets.getBrowser(super.getRequest(), "ie");
        if (ie != null && ie < 8.0) {
            Clients.showNotification("This demo does not support IE6/7", true);
        }
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        tenderService = context.getBean(TenderService.class);
        tenderCommentService = context.getBean(TenderCommentService.class);
        viewNewTender = super.isPublisher();
        commentList = new HashMap<Tender, List<TenderComment>>();
        commentNumbers = new HashMap<Tender, Integer>();
        tenderSearchCriteria = new TenderSearchCriteria();
        this.setActivePage(this.tenderSearchCriteria.getStartIndex());
        loadTenders();
        updateTenderComments();
        desktop = Executions.getCurrent().getDesktop();
        if(timer!=null)
            timer.cancel();
        timer = new Timer();
        timer.schedule(updatePosts(),0,1000);
        addCommonTags((PageCtrl) view.getPage());
    }



    public String toString(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE MMMM, dd yyyy");
        return  sdf.format(date);
    }

    public void loadTenders(){
        this.setTotalSize(this.tenderService.countTenders(tenderSearchCriteria));
        this.tenderSearchCriteria.setStartIndex(getActivePage());
        List<Tender> tenders = tenderService.listTenders(tenderSearchCriteria);
        for(Tender tender : tenders){
            List<TenderComment> tenderComments = tenderCommentService.listCommentsByTender(tender);
            tender.setTenderComments(tenderComments);
            commentList.put(tender, tenderComments);
            commentNumbers.put(tender, tenderComments.size());
        }
        tenderModel = tenders;
        BindUtils.postNotifyChange(null, null, this, "tenderModel");
        BindUtils.postNotifyChange(null, null, this, "commentNumbers");
        BindUtils.postNotifyChange(null, null, this, "commentList");
    }


    @Command
    @NotifyChange({"activePage", "totalSize", "tenderSearchCriteria", "tenderModel"})
    public void clearFilters() {
        this.tenderSearchCriteria.clear();
        this.tenderSearchCriteria.setPageOrderMode("desc");
        this.tenderSearchCriteria.clearFilters();
        loadTenders();
    }

    @Command
    @NotifyChange({"activePage", "totalSize", "tenderSearchCriteria", "tenderModel"})
    public void filter() {
        this.tenderSearchCriteria.clear();
        this.tenderSearchCriteria.setPageOrderMode("desc");
        this.activePage = this.tenderSearchCriteria.getStartIndex();
        loadTenders();
    }

    @NotifyChange({"activePage", "totalSize", "tenderSearchCriteria", "tenderModel"})
    public void setActivePage(Integer activePage) {
        this.activePage = activePage;
        loadTenders();
    }

    public void updateTenderComments(){
        for(Tender tender : tenderModel){
            List<TenderComment> tenderComments = tenderCommentService.listCommentsByTender(tender);
            tender.setTenderComments(tenderComments);
            commentList.put(tender, tenderComments);
            commentNumbers.put(tender, tenderComments.size());
        }
        BindUtils.postNotifyChange(null, null, this, "commentNumbers");
        BindUtils.postNotifyChange(null, null, this, "commentList");
    }

    private int tenderSize(){
        List<Tender> tenders = tenderService.listTenders(tenderSearchCriteria);
        return tenders.size();
    }

    public String getAuthorImage(Tender tender){
        String ret = "";

        if (tender.getAuthor() != null && tender.getAuthor().getImage() != null)
            ret += MyProperties.getInstance().getImagePath() + "/staff/" + tender.getAuthor().getImage();

        return ret;
    }

    private boolean update(){
        if(tenderSize()!=tenderModel.size())
            return true;
        return false;
    }


    @Command
    public void newTender(){
        Window window = (Window) Executions.createComponents("/pages/editor/tender-editor.zul", null, null);
        window.doModal();
    }

    @Command
    public void editTender(@BindingParam("tender")Tender tender){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("tender", tender);
        Window window = (Window) Executions.createComponents("/pages/editor/tender-editor.zul", null, map);
        window.doModal();

    }

    @Command
    public void deleteTender(@BindingParam("tender")Tender tender){
        for(TenderComment tenderComment : tender.getTenderComments()) {
            tenderCommentService.removeComment(tenderComment);
        }
        tender.getTenderComments().removeAll(tender.getTenderComments());
        tenderService.removeTender(tender);
    }

    @Command
    @NotifyChange({"commentList"})
    public void comment(@BindingParam("tender") Tender tender) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("tender",tender);
        Window window = (Window) Executions.createComponents("/pages/tender/tender-comment.zul", null, map);
        window.doModal();
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
                            loadTenders();
                        updateTenderComments();
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

    public List<Tender> getTenderModel() {
        return tenderModel;
    }

    public void setTenderModel(List<Tender> tenderModel) {
        this.tenderModel = tenderModel;
    }

    public boolean isViewNewTender() {
        return viewNewTender;
    }

    public void setViewNewTender(boolean viewNewTender) {
        this.viewNewTender = viewNewTender;
    }

    public Map<Tender, List<TenderComment>> getCommentList() {
        return commentList;
    }

    public void setCommentList(Map<Tender, List<TenderComment>> commentList) {
        this.commentList = commentList;
    }

    public Map<Tender, Integer> getCommentNumbers() {
        return commentNumbers;
    }

    public void setCommentNumbers(Map<Tender, Integer> commentNumbers) {
        this.commentNumbers = commentNumbers;
    }

    public TenderSearchCriteria getTenderSearchCriteria() {
        return tenderSearchCriteria;
    }

    public void setTenderSearchCriteria(TenderSearchCriteria tenderSearchCriteria) {
        this.tenderSearchCriteria = tenderSearchCriteria;
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
}
