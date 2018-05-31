package qa.tecnositafgulf.viewmodel.inventory.location;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.sys.PageCtrl;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;
import qa.tecnositafgulf.model.inventory.location.Location;
import qa.tecnositafgulf.searchcriteria.inventory.LocationSearchCriteria;
import qa.tecnositafgulf.service.InventoryService;
import qa.tecnositafgulf.spring.config.AppConfig;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewLocationViewModel  extends IntranetVM {
    private LocationSearchCriteria locationSearchCriteria;
    private InventoryService inventoryService;
    private List<Location> locationList;
    private Integer totalSize;
    private Integer activePage;

    @AfterCompose
    public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        inventoryService = context.getBean(InventoryService.class);
        locationSearchCriteria = new LocationSearchCriteria();
        this.setActivePage(this.locationSearchCriteria.getStartIndex());
        loadData();
        addCommonTags((PageCtrl) view.getPage());
    }

    private void loadData() {
        this.setTotalSize(this.inventoryService.getAllLocationsCount(locationSearchCriteria));
        this.locationSearchCriteria.setStartIndex(getActivePage());
        locationList = inventoryService.getAllLocations(locationSearchCriteria);
    }

    @Command
    @NotifyChange({"activePage", "totalSize", "locationSearchCriteria", "locationList"})
    public void order(@BindingParam("orderBy") String orderBy) {
        this.locationSearchCriteria.setOrderByFieldName(orderBy);
        this.locationSearchCriteria.setPageOrderMode("asc".equalsIgnoreCase(this.locationSearchCriteria.getPageOrderMode()) ? "desc" : "asc");
        //load method call to load data
        loadData();
    }

    @Command
    @NotifyChange({"activePage", "totalSize", "locationSearchCriteria", "locationList"})
    public void clearFilters() {
        this.locationSearchCriteria.clear();
        this.locationSearchCriteria.setPageOrderMode("desc");
        this.locationSearchCriteria.clearFilters();
        loadData();
    }

    @Command
    @NotifyChange({"activePage", "totalSize", "locationSearchCriteria", "locationList"})
    public void filter() {
        this.locationSearchCriteria.clear();
        this.locationSearchCriteria.setPageOrderMode("desc");
        this.activePage = this.locationSearchCriteria.getStartIndex();
        loadData();
    }


    @Command
    @NotifyChange("locationList")
    public void add() {
        final Map<String, Location> params = new HashMap<String, Location>();
        params.put("locationToModify", null);
        ((Window) Executions.getCurrent().createComponents("/pages/inventory/location/saveLocation.zul", null, params)).doModal();
    }

    @Command
    @NotifyChange("locationList")
    public void modify(@BindingParam("item") Location location) {
        final Map<String, Location> params = new HashMap<String, Location>();
        params.put("locationToModify", location);
        ((Window) Executions.getCurrent().createComponents("/pages/inventory/location/saveLocation.zul", null, params)).doModal();
    }

    @Command
    @NotifyChange("locationList")
    public void delete(@BindingParam("item") final Location location) {
        Messagebox.show("Do you want to delete this Location?", "Warning", Messagebox.OK | Messagebox.CANCEL, Messagebox.INFORMATION, new EventListener<Event>() {
            public void onEvent(Event event) throws Exception {
                if (event.getName().equals("onOK")) {
                    try {
                        inventoryService.remove(location);
                        locationList.remove(location);
                        loadData();
                    } catch (Exception exception) {
                        Messagebox.show("Cannot delete this location because atleast one product is attached", "Warning", Messagebox.OK | Messagebox.CANCEL, Messagebox.INFORMATION, new EventListener<Event>() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                if (event.getName().equals("onOK")) {

                                }
                            }
                        });

                    }
                    //loadData();
                    Executions.sendRedirect("/pages/inventory/location/viewLocation.zul");
                }
            }
        });
    }

    public LocationSearchCriteria getLocationSearchCriteria() {
        return locationSearchCriteria;
    }

    public void setLocationSearchCriteria(LocationSearchCriteria locationSearchCriteria) {
        this.locationSearchCriteria = locationSearchCriteria;
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

    @NotifyChange({"activePage", "totalSize", "locationSearchCriteria", "locationList"})
    public void setActivePage(Integer activePage) {
        this.activePage = activePage;
        loadData();
    }

    public InventoryService getInventoryService() {
        return inventoryService;
    }

    public void setInventoryService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public List<Location> getLocationList() {
        return locationList;
    }

    public void setLocationList(List<Location> locationList) {
        this.locationList = locationList;
    }
}
