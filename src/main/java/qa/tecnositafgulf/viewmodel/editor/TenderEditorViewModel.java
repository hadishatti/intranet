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
import qa.tecnositafgulf.model.tenders.Tender;
import qa.tecnositafgulf.service.TenderService;
import qa.tecnositafgulf.spring.config.AppConfig;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * Created by hadi on 12/18/17.
 */
public class TenderEditorViewModel  extends IntranetVM {

    private TenderService tenderService;
    private String content = "";
    private Tender tenderToEdit=null;
    private String link="";
    private String title="";
    private String number="";
    private List<String> types;
    private String selectedType;
    private Date issuingDate;
    private Date closingDate;

    @AfterCompose
    public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view){
        Selectors.wireComponents(view, this, false);
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        tenderService = context.getBean(TenderService.class);
        Map map = Executions.getCurrent().getArg();
        tenderToEdit = (Tender) map.get("tender");
        types = IntranetProperties.TenderTypes.getAll();
        selectedType = IntranetProperties.TenderTypes.ICT.toString();
        if(tenderToEdit!=null) {
            title = tenderToEdit.getTitle();
            content = tenderToEdit.getContent();
            number = tenderToEdit.getNumber();
            selectedType = tenderToEdit.getType();
            issuingDate = tenderToEdit.getIssuingDate();
            closingDate = tenderToEdit.getClosingDate();
            link = tenderToEdit.getLink();
        }
        addCommonTags((PageCtrl) view.getPage());
    }



    protected void showInfo(String message) {
        Messagebox.show(message, "Error!", Messagebox.OK, Messagebox.INFORMATION);
    }

    @Command
    public void post(@BindingParam("content") String content){
        this.content = content;

        Tender tender;
        if(tenderToEdit!=null)
            tender = tenderToEdit;
        else
            tender = new Tender();
        tender.setTitle(title);
        tender.setNumber(number);
        tender.setIssuingDate(issuingDate);
        tender.setClosingDate(closingDate);
        tender.setLink(link);
        tender.setType(selectedType);
        tender.setContent(content);
        tender.setDate(new Date());
        tender.setAuthor(((Employee) Sessions.getCurrent().getAttribute("employee")));
        try {
            tenderService.saveTender(tender);
        }catch (Exception e){
            Messagebox.show("An Error Occured! Please contact the System Administrator.","Error", Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }
        Messagebox.show("Tender Posted!", "Information", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
            public void onEvent(org.zkoss.zk.ui.event.Event event) throws Exception {
                Executions.sendRedirect("/pages/tender/view-tenders.zul");
            }
        });
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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

    public Date getIssuingDate() {
        return issuingDate;
    }

    public void setIssuingDate(Date issuingDate) {
        this.issuingDate = issuingDate;
    }

    public Date getClosingDate() {
        return closingDate;
    }

    public void setClosingDate(Date closingDate) {
        this.closingDate = closingDate;
    }

}
