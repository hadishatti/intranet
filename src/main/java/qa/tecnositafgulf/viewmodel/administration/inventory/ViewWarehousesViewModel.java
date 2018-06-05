package qa.tecnositafgulf.viewmodel.administration.inventory;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.sys.PageCtrl;
import qa.tecnositafgulf.model.administration.inventory.Warehouse;
import qa.tecnositafgulf.service.AuthenticationService;
import qa.tecnositafgulf.service.InventoryService;
import qa.tecnositafgulf.spring.config.AppConfig;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import java.util.List;

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
}
