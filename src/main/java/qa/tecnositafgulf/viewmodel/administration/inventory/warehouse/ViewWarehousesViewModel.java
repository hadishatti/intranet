package qa.tecnositafgulf.viewmodel.administration.inventory.warehouse;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.sys.PageCtrl;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;
import qa.tecnositafgulf.model.administration.inventory.Warehouse;
import qa.tecnositafgulf.service.AuthenticationService;
import qa.tecnositafgulf.service.InventoryService;
import qa.tecnositafgulf.spring.config.AppConfig;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ledio on 6/5/18.
 */
public class ViewWarehousesViewModel extends IntranetVM {

    private List<Warehouse> warehouses;
    private InventoryService inventoryService;

    @AfterCompose
    public void createAndPopulateHtml(@ContextParam(ContextType.VIEW) Component view){
        init();
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        inventoryService = context.getBean(InventoryService.class);
        authenticationService = context.getBean(AuthenticationService.class);
        warehouses = inventoryService.getWarehouseList();
    }

    @Command
    @NotifyChange("profiles")
    public void modify(@BindingParam("item") Warehouse warehouse){
        final Map<String, Warehouse> params = new HashMap<String, Warehouse>();
        params.put("profileToModify", warehouse);
        ((Window) Executions.getCurrent().createComponents("/pages/administration/inventory/warehouses/saveWarehouse.zul", null, params)).doModal();
    }

    @Command
    @NotifyChange("warehouses")
    public void delete(@BindingParam("item") final Warehouse warehouse){
        //TODO an other procedure might be needed considering there might be items in the warehouse
    }

    @Command
    @NotifyChange("warehouses")
    public void add(){
        final Map<String, Warehouse> params = new HashMap<String, Warehouse>();
        params.put("warehouseToModify", null);
        ((Window) Executions.getCurrent().createComponents("/pages/administration/inventory/warehouses/saveWarehouse.zul", null, params)).doModal();
    }

    @Command
    public void viewInventory(@BindingParam("item") Warehouse warehouse){
        final Map<String, Warehouse> params = new HashMap<String, Warehouse>();
        params.put("warehouse", warehouse);
        ((Window) Executions.getCurrent().createComponents("/pages/administration/inventory/viewInventoryLog.zul", null, params)).doModal();
    }

    public List<Warehouse> getWarehouses() {
        return warehouses;
    }

    public void setWarehouses(List<Warehouse> warehouses) {
        this.warehouses = warehouses;
    }
}
