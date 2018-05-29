package qa.tecnositafgulf.viewmodel.administration.settings;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.DesktopUnavailableException;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;
import qa.tecnositafgulf.config.MyProperties;
import qa.tecnositafgulf.model.properties.Property;
import qa.tecnositafgulf.searchcriteria.PropertySearchCriteria;
import qa.tecnositafgulf.service.PropertyService;
import qa.tecnositafgulf.spring.config.AppConfig;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import java.util.*;

/**
 * Created by ameljo on 5/9/18.
 */
public class SettingsViewModel extends IntranetVM {

    private List<Property> propertyModel;
    private PropertyService propertyService;
    private PropertySearchCriteria propertySearchCriteria;
    private Timer timer;
    private Desktop desktop;
    private String propertyPath;
    private Integer activePage;
    private Integer totalSize;

    @AfterCompose
    public void afterCompose(){
        init();
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        propertyService = context.getBean(PropertyService.class);
        propertySearchCriteria = new PropertySearchCriteria();
        this.setActivePage(this.propertySearchCriteria.getStartIndex());
        desktop = Executions.getCurrent().getDesktop();
        loadProperties();
        if(timer!=null)
            timer.cancel();
        timer = new Timer();
        timer.schedule(updatePosts(),0,1000);
    }

    public void loadProperties(){
        this.setTotalSize(this.propertyService.countProperty(propertySearchCriteria));
        this.propertySearchCriteria.setStartIndex(getActivePage());
        propertyModel = propertyService.getAllProperties(propertySearchCriteria);
        BindUtils.postNotifyChange(null, null, this, "propertyModel");
    }


    @Command
    @NotifyChange({"activePage", "totalSize", "propertySearchCriteria", "propertyModel"})
    public void order(@BindingParam("orderBy") String orderBy){
        this.propertySearchCriteria.setOrderByFieldName(orderBy);
        this.propertySearchCriteria.setPageOrderMode("asc".equalsIgnoreCase(this.propertySearchCriteria.getPageOrderMode()) ? "desc" : "asc");
        loadProperties();
    }

    @NotifyChange({"activePage", "totalSize", "propertySearchCriteria", "propertyModel"})
    public void setActivePage(Integer activePage) {
        this.activePage = activePage;
        loadProperties();
    }

    private int propertySize(){
        List<Property> properties = propertyService.getAllProperties(propertySearchCriteria);
        return properties.size();
    }

    private boolean update(){
        if(propertySize()!=propertyModel.size())
            return true;
        return false;
    }

    @Command
    @NotifyChange({"activePage", "totalSize", "propertySearchCriteria", "propertyModel"})
    public void clearFilters() {
        this.propertySearchCriteria.clear();
        this.propertySearchCriteria.setPageOrderMode("desc");
        this.propertySearchCriteria.clearFilters();
        loadProperties();
    }

    @Command
    @NotifyChange({"activePage", "totalSize", "propertySearchCriteria", "propertyModel"})
    public void filter() {
        this.propertySearchCriteria.clear();
        this.propertySearchCriteria.setPageOrderMode("desc");
        this.activePage = this.propertySearchCriteria.getStartIndex();
        loadProperties();
    }

    @Command
    public void changeProperties(){
        for(Property property : propertyModel) {
            propertyPath = formatPath(property.getValue());
            property.setValue(propertyPath);
            MyProperties.getInstance().saveProperty(property);
        }
        Executions.sendRedirect("/pages/home.zul");
    }

    @Command("delete")
    public void deleteProperty(@BindingParam("item") final Property property){
        Messagebox.show("Do you want to delete this property?", "Warning", Messagebox.OK | Messagebox.CANCEL, Messagebox.INFORMATION, new EventListener<Event>() {
            public void onEvent(Event event) throws Exception {
                if (event.getName().equals("onOK")) {
                    MyProperties.getInstance().deleteProperty(property);
                }
            }
        });
    }

    @Command("addNew")
    public void addProperty(){
        ((Window) Executions.getCurrent().createComponents("/pages/administration/settings/saveSettings.zul", null, null)).doModal();
    }


    @Command
    public void modify(@BindingParam("item") Property property){
        final Map<String, Property> params = new HashMap<String, Property>();
        params.put("propertyToModify", property);
        ((Window) Executions.getCurrent().createComponents("/pages/administration/settings/saveSettings.zul", null, params)).doModal();
    }

    private String formatPath(String path) {
        String ret = "";

        if(path.substring(path.length() - 1).equals("/"))
            ret = path.substring(0, path.length() - 1);
        else
            ret = path;

        return ret;
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
                            loadProperties();
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

    public Integer getActivePage() {
        return activePage;
    }

    public Integer getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Integer totalSize) {
        this.totalSize = totalSize;
    }

    public List<Property> getPropertyModel() {
        return propertyModel;
    }

    public void setPropertyModel(List<Property> propertyModel) {
        this.propertyModel = propertyModel;
    }

    public PropertySearchCriteria getPropertySearchCriteria() {
        return propertySearchCriteria;
    }

    public void setPropertySearchCriteria(PropertySearchCriteria propertySearchCriteria) {
        this.propertySearchCriteria = propertySearchCriteria;
    }

    public String getPropertyPath() {
        return propertyPath;
    }

    public void setPropertyPath(String propertyPath) {
        this.propertyPath = propertyPath;
    }
}
