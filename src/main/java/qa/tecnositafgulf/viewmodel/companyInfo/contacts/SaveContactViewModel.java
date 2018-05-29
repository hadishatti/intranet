package qa.tecnositafgulf.viewmodel.companyInfo.contacts;

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
import org.zkoss.zul.Image;
import org.zkoss.zul.Messagebox;
import qa.tecnositafgulf.config.MyProperties;
import qa.tecnositafgulf.model.companyInfo.Contact;
import qa.tecnositafgulf.service.CompanyInfoService;
import qa.tecnositafgulf.spring.config.AppConfig;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import javax.persistence.PersistenceException;
import java.io.UnsupportedEncodingException;

/**
 * Created by ameljo on 5/13/18.
 */
public class SaveContactViewModel extends IntranetVM {

    @Wire
    private Image image;

    private CompanyInfoService service;
    private Contact contact;
    private boolean uploaded = false;
    private String link = "";
    private boolean modify = false;
    private boolean currentUser = false;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("contactToModify") Contact contactToModify) {
        init();
        Selectors.wireComponents(view, this, false);
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        service = context.getBean(CompanyInfoService.class);
        if(contactToModify!=null){
            modify = true;
            contact = contactToModify;
            if(contact.equals((Contact) Sessions.getCurrent().getAttribute("contact")))
                currentUser=true;
            image.setSrc(contact.getImage());
            if(!contact.getImage().contains("data:image")) {
                link = contact.getImage();
                if (link.equals("NONE"))
                    image.setSrc(MyProperties.getInstance().getImagePath() + "/" + link + ".png");
            }
        }else{
            modify = false;
            contact = new Contact();
        }

    }

    @Command
    @NotifyChange({"image"})
    public void link() {
        image.setSrc(link);
        uploaded=false;
    }

    @Command
    @NotifyChange({"image"})
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
                    image.setContent((AImage) media);//Initialize the bind object to show image in zul page and Notify it also
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
    public void saveContact(){
            if(!modify) {
                if(contact.getName().isEmpty()){
                    Messagebox.show("Name Cannot be Empty!", "Error", Messagebox.OK, Messagebox.EXCLAMATION);
                    return;
                }
                if(contact.getPosition().isEmpty()){
                    Messagebox.show("Position Cannot be Empty!", "Error", Messagebox.OK, Messagebox.EXCLAMATION);
                    return;
                }
            }

        try {
            if(uploaded)
                contact.setImage("data:image/png;base64,"+new String(Base64.encode(image.getContent().getByteData()),"UTF-8"));
            else {
                if (image.getSrc() != null && !image.getSrc().isEmpty() && !image.getSrc().equals(MyProperties.getInstance().getImagePath() + "/NONE.png"))
                    contact.setImage(image.getSrc());
                else
                    contact.setImage("NONE");
            }
        } catch (UnsupportedEncodingException e) {
            showInfo("Error during saving the image!");
        }

        try {
            service.saveContact(contact);
            Messagebox.show("OK! Do you want to view all the Contacts?", "Information", Messagebox.OK | Messagebox.CANCEL, Messagebox.INFORMATION, new EventListener<Event>() {
                public void onEvent(Event event) throws Exception {
                    if (event.getName().equals("onOK")) {
                        Executions.sendRedirect("/pages/company-info/contacts/viewContacts.zul");
                    }
                }
            });
        }catch (PersistenceException persistenceException){
            Messagebox.show("User already exist with same name", "Information", Messagebox.OK | Messagebox.CANCEL, Messagebox.INFORMATION);
        }
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
