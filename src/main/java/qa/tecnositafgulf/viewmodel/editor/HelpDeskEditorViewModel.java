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
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.sys.PageCtrl;
import org.zkoss.zul.Image;
import org.zkoss.zul.Messagebox;
import qa.tecnositafgulf.model.administration.Employee;
import qa.tecnositafgulf.model.helpDesk.HelpDesk;
import qa.tecnositafgulf.service.HelpDeskService;
import qa.tecnositafgulf.spring.config.AppConfig;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

/**
 * Created by klajdi on 20/05/18.
 */
public class HelpDeskEditorViewModel extends IntranetVM{
    @Wire
    private Image imagePreview;

    private HelpDeskService helpDeskService;
    private String content = "";
    private HelpDesk helpDeskToEdit =null;
    private String link="";
    private int imageWidth;
    private String title;
    private boolean uploaded=false;

    @AfterCompose
    public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view){
        Selectors.wireComponents(view, this, false);
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        helpDeskService = context.getBean(HelpDeskService.class);
        Map map = Executions.getCurrent().getArg();
        helpDeskToEdit = (HelpDesk) map.get("helpDesk");
        if(helpDeskToEdit !=null) {
            title = helpDeskToEdit.getTitle();
            content = helpDeskToEdit.getContent();
            if(helpDeskToEdit.getImage() == null){

            }
            imagePreview.setSrc(helpDeskToEdit.getImage());
            if(!helpDeskToEdit.getImage().contains("data:image"))
                link = helpDeskToEdit.getImage();
            imageWidth = helpDeskToEdit.getImageWidth();
        }

        if(imagePreview.getSrc()==null || imagePreview.getSrc().isEmpty())
            imageWidth=400;
        addCommonTags((PageCtrl) view.getPage());
    }

    @Command
    @NotifyChange({"imageWidth", "imagePreview"})
    public void link() {
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

        HelpDesk helpDesk;
        if(helpDeskToEdit !=null)
            helpDesk = helpDeskToEdit;
        else
            helpDesk = new HelpDesk();
        helpDesk.setTitle(title);
        try {
            if(uploaded)
                helpDesk.setImage("data:image/png;base64,"+new String(Base64.encode(imagePreview.getContent().getByteData()),"UTF-8"));
            else {
                if(imagePreview.getSrc() != null && !imagePreview.getSrc().isEmpty())
                    helpDesk.setImage(imagePreview.getSrc());
                else {
                    helpDesk.setImage("NONE");
                    imageWidth=0;
                }
            }
        } catch (UnsupportedEncodingException e) {
            showInfo("Error during saving the image!");
        }
        helpDesk.setImageWidth(imageWidth);
        helpDesk.setContent(content);
        helpDesk.setDate(new Date());
        helpDesk.setAuthor(((Employee) Sessions.getCurrent().getAttribute("employee")));
        try {
            helpDeskService.saveHelpDesk(helpDesk);
        }catch (Exception e){
            Messagebox.show("An Error Occured! Please contact the System Administrator.","Error", Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }
        Messagebox.show("HelpDesk Posted!", "Information", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
            public void onEvent(org.zkoss.zk.ui.event.Event event) throws Exception {
                Executions.sendRedirect("/pages/helpDesk/view-helpDesk.zul");
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
