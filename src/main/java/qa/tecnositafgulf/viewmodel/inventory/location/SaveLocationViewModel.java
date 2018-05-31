package qa.tecnositafgulf.viewmodel.inventory.location;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.sys.PageCtrl;
import org.zkoss.zul.Messagebox;
import qa.tecnositafgulf.model.administration.Company;
import qa.tecnositafgulf.model.inventory.location.Location;
import qa.tecnositafgulf.service.AdministrationService;
import qa.tecnositafgulf.service.InventoryService;
import qa.tecnositafgulf.spring.config.AppConfig;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import javax.persistence.PersistenceException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class SaveLocationViewModel  extends IntranetVM {

    private InventoryService inventoryService;
    private List<Company> companyList;
    private Company selectedCompany;
    private Location location;

    @AfterCompose
    public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view,
                               @ExecutionArgParam("locationToModify") Location location){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        inventoryService = context.getBean(InventoryService.class);
        AdministrationService administrationService = context.getBean(AdministrationService.class);
        companyList = administrationService.listCompanies();
        if(location != null){
            this.location = location;
            this.selectedCompany = this.location.getCompany();
        }else{
            this.location = new Location();
        }
        addCommonTags((PageCtrl) view.getPage());
    }

    @Command
    public void saveLocation() {
        try {
            location.setCreateDate(new Timestamp(new Date().getTime()));
            location.setCompany(selectedCompany);
            inventoryService.saveLocation(location);
            Messagebox.show("OK! Do you want to view all the Company Locations?", "Warning", Messagebox.OK | Messagebox.CANCEL, Messagebox.INFORMATION, new EventListener<Event>() {
                public void onEvent(Event event) throws Exception {
                    if (event.getName().equals("onOK")) {
                        Executions.sendRedirect("/pages/inventory/location/viewLocation.zul");
                    }
                }
            });
        }catch (PersistenceException persistenceException){
            Messagebox.show("Location with same name already exits", "Warning", Messagebox.OK | Messagebox.CANCEL, Messagebox.INFORMATION);
        }
    }

    public InventoryService getInventoryService() {
        return inventoryService;
    }

    public void setInventoryService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public List<Company> getCompanyList() {
        return companyList;
    }

    public void setCompanyList(List<Company> companyList) {
        this.companyList = companyList;
    }

    public Company getSelectedCompany() {
        return selectedCompany;
    }

    public void setSelectedCompany(Company selectedCompany) {
        this.selectedCompany = selectedCompany;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
