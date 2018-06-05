package qa.tecnositafgulf.viewmodel.administration.inventory.inventoryLog;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.sys.PageCtrl;
import qa.tecnositafgulf.config.inventoryEnums.InventoryActionEnum;
import qa.tecnositafgulf.model.administration.inventory.InventoryLog;
import qa.tecnositafgulf.service.InventoryService;
import qa.tecnositafgulf.spring.config.AppConfig;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import java.util.List;

/**
 * Created by ledio on 6/5/18.
 */
public class ViewInventoryLogViewModel extends IntranetVM {

    private InventoryService service;
    private List<InventoryLog> inventoryLogs;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        init();
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        service = context.getBean(InventoryService.class);
        inventoryLogs = service.getAllInventoryLogs();

        addCommonTags((PageCtrl) view.getPage());
    }

    @Command
    public void viewItems(@BindingParam("item") InventoryLog inventoryLog){
        //TODO implement after figuring what is best to send as param to view Items
    }

    public boolean isViewItems(InventoryLog inventoryLog){
        if (
                inventoryLog.getAction().equals(InventoryActionEnum.BULK_ITEM_ADDITION) ||
                inventoryLog.getAction().equals(InventoryActionEnum.BULK_ITEM_REMOVAL) ||
                inventoryLog.getAction().equals(InventoryActionEnum.ITEM_ADDITION) ||
                inventoryLog.getAction().equals(InventoryActionEnum.ITEM_REMOVAL) ||
                inventoryLog.getAction().equals(InventoryActionEnum.TRANSFER_ITEMS)
                )
            return true;
        return false;
    }

    //TODO other functionality might be needed

    public List<InventoryLog> getInventoryLogs() {
        return inventoryLogs;
    }

    public void setInventoryLogs(List<InventoryLog> inventoryLogs) {
        this.inventoryLogs = inventoryLogs;
    }
}
