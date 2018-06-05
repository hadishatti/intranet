package qa.tecnositafgulf.viewmodel.administration.inventory.warehouse;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.sys.PageCtrl;
import org.zkoss.zul.Messagebox;
import qa.tecnositafgulf.model.administration.inventory.Warehouse;
import qa.tecnositafgulf.service.InventoryService;
import qa.tecnositafgulf.spring.config.AppConfig;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import javax.persistence.PersistenceException;

/**
 * Created by ledio on 6/5/18.
 */
public class SaveWarehouseViewModel extends IntranetVM {

    private Warehouse warehouse;
    private boolean modify;
    private InventoryService service;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("warehouseToModify") Warehouse warehouseToModify) {
        init();
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        service = context.getBean(InventoryService.class);
        if (warehouseToModify != null) {
            modify=true;
            warehouse = warehouseToModify;
        } else {
            modify=false;
            warehouse = new Warehouse();
        }

        addCommonTags((PageCtrl) view.getPage());
    }

    //TODO add constraints for mandatory fields

    @Command
    @NotifyChange("warehouse")
    public void saveWarehouse() {
        try{
            service.saveWarehouse(warehouse);
            Messagebox.show("OK! Do you want to view all Warehouses?", "Warning", Messagebox.OK | Messagebox.CANCEL, Messagebox.INFORMATION, new EventListener<Event>() {
                public void onEvent(Event event) throws Exception {
                    if (event.getName().equals("onOK")) {
                        Executions.sendRedirect("/pages/administration/inventory/warehouses/viewWarehouses.zul");
                    }
                }
            });
        }catch (PersistenceException persistenceException){
            Messagebox.show("Warehouse with same name already exits", "Warning", Messagebox.OK , Messagebox.ERROR);
        }
    }
}
