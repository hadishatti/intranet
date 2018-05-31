package qa.tecnositafgulf.controller;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import qa.tecnositafgulf.config.MyProperties;
import qa.tecnositafgulf.model.events.CompanyEvent;
import qa.tecnositafgulf.service.EventService;
import qa.tecnositafgulf.spring.config.AppConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hadi on 1/6/18.
 */
public class EventSliderController extends GenericForwardComposer {
    Image eventImage;
    Label eventDescription;
    List<String> contentList = null;
    List<String> descriptionList = null;
    EventService eventService;
    private List<CompanyEvent> companyEvents;

    public void doAfterCompose (Component comp) throws Exception {
        super.doAfterCompose(comp);
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        eventService = context.getBean(EventService.class);
        companyEvents = eventService.listEvents();
        if(companyEvents.size()==0){
            eventDescription.setValue("NO EVENT COMPANY SO FAR");
            eventImage.setSrc(MyProperties.getInstance().getImagePath() + "/gbdg-logo.jpeg");
        }
        else {
            // init first part image
            eventDescription.setValue(companyEvents.get(0).getTitle());
            String image = companyEvents.get(0).getImage();
            if (image == null)
                image = MyProperties.getInstance().getImagePath() +  "/gbdg-logo.jpeg";
            eventImage.setSrc(image);
            Clients.evalJavaScript("setEventContent('" + getContentString() + "')");
            Clients.evalJavaScript("setEventDescription('" + getDescriptionString() + "')");
            int delay = 5;
            if (companyEvents.size() > 1) {
                String command = "startEventSlideShow(" + delay * 1000 + ")";
                System.out.println(command);
                Clients.evalJavaScript(command);
            }
        }
    }

    private String getContentString() {
        StringBuilder sb = new StringBuilder();
        List l = getContentList();
        for (int i = 0; i < l.size(); i++) {
            if (i > 0)
                sb.append("$$$");
            sb.append(l.get(i));
        }
        return sb.toString();
    }
    private String getDescriptionString() {
        StringBuilder sb = new StringBuilder();
        List l = getDescriptionList();
        for (int i = 0; i < l.size(); i++) {
            if (i > 0)
                sb.append("$$$");
            sb.append(l.get(i));
        }
        return sb.toString();
    }

    private List<String> getContentList () {
        if (contentList == null) {
            contentList = new ArrayList<String>();
            for (int i = 0; i < companyEvents.size(); i++) {
                String image = companyEvents.get(i).getImage();
                if(image==null)
                    image =MyProperties.getInstance().getImagePath() +  "/gbdg-logo.jpeg";
                contentList.add(image);
            }
        }
        return contentList;
    }

    private List<String> getDescriptionList () {
        if (descriptionList == null) {
            descriptionList = new ArrayList<String>();
            for (int i = 0; i < companyEvents.size(); i++) {
                descriptionList.add(companyEvents.get(i).getTitle());
            }
        }
        return descriptionList;
    }

    @Listen("onClick = #descriptionDiv; onClick = #container")
    public void onClick(){
        Executions.sendRedirect("/pages/companyEvent/view-companyEvents.zul");
    }
}
