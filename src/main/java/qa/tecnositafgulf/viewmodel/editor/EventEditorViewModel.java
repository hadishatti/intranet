package qa.tecnositafgulf.viewmodel.editor;

import org.apache.xmlbeans.impl.util.Base64;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.annotation.*;
import org.zkoss.image.AImage;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Image;
import org.zkoss.zul.Messagebox;
import qa.tecnositafgulf.model.administration.Employee;
import qa.tecnositafgulf.model.events.CompanyEvent;
import qa.tecnositafgulf.service.EventService;
import qa.tecnositafgulf.spring.config.AppConfig;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

/**
 * Created by hadi on 12/18/17.
 */
public class EventEditorViewModel  extends IntranetVM {
    @Wire
    private Image imagePreview;

    private EventService eventService;
    private String content = "";
    private CompanyEvent companyEventToEdit =null;
    private String link="";
    private int imageWidth;
    private String title;
    private boolean uploaded=false;

    @AfterCompose
    public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view){
        Selectors.wireComponents(view, this, false);
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        eventService = context.getBean(EventService.class);
        Map map = Executions.getCurrent().getArg();
        companyEventToEdit = (CompanyEvent) map.get("companyEvent");
        if(companyEventToEdit !=null) {
            title = companyEventToEdit.getTitle();
            content = companyEventToEdit.getContent();
            imagePreview.setSrc(companyEventToEdit.getImage());
            if(!companyEventToEdit.getImage().contains("data:image"))
                link = companyEventToEdit.getImage();
            imageWidth = companyEventToEdit.getImageWidth();
        }
        
        if(imagePreview.getSrc()==null || imagePreview.getSrc().isEmpty())
            imageWidth=400;
    }

    @Command
    @NotifyChange({"imageWidth", "imagePreview"})
    public void link() {
        imageWidth=400;
        imagePreview.setSrc(link);
        uploaded=false;
    }

    @Command
    @NotifyChange({"imageWidth","imagePreview"})
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
                    imageWidth=400;
                    imagePreview.setContent((AImage) media);//Initialize the bind object to show image in zul page and Notify it also
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
    public void post(@BindingParam("content") String content){
        this.content = content;

        CompanyEvent companyEvent;
        if(companyEventToEdit !=null)
            companyEvent = companyEventToEdit;
        else
            companyEvent = new CompanyEvent();
        companyEvent.setTitle(title);
        try {
            if(uploaded)
                companyEvent.setImage("data:image/png;base64,"+new String(Base64.encode(imagePreview.getContent().getByteData()),"UTF-8"));
            else {
                if(imagePreview.getSrc() != null && !imagePreview.getSrc().isEmpty())
                    companyEvent.setImage(imagePreview.getSrc());
                else {
                    companyEvent.setImage("NONE");
                    imageWidth=0;
                }
            }
        } catch (UnsupportedEncodingException e) {
            showInfo("Error during saving the image!");
        }
        companyEvent.setImageWidth(imageWidth);
        companyEvent.setContent(content);
        companyEvent.setDate(new Date());
        companyEvent.setAuthor(((Employee) Sessions.getCurrent().getAttribute("employee")));
        try {
            eventService.saveEvent(companyEvent);
        }catch (Exception e){
            Messagebox.show("An Error Occured! Please contact the System Administrator.","Error", Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }
        Messagebox.show("Company Event Posted!", "Information", Messagebox.OK, Messagebox.INFORMATION, new EventListener<org.zkoss.zk.ui.event.Event>() {
            public void onEvent(org.zkoss.zk.ui.event.Event event) throws Exception {
                Executions.sendRedirect("/pages/companyEvent/view-companyEvents.zul");
            }
        });
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Image getImagePreview() {
        return imagePreview;
    }

    public void setImagePreview(Image imagePreview) {
        this.imagePreview = imagePreview;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
