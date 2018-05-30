package qa.tecnositafgulf.viewmodel.inventory.supplier;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.sys.PageCtrl;
import org.zkoss.zul.Messagebox;
import qa.tecnositafgulf.model.inventory.supplier.ProductSupplier;
import qa.tecnositafgulf.service.InventoryService;
import qa.tecnositafgulf.spring.config.AppConfig;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import javax.persistence.PersistenceException;
import java.sql.Timestamp;
import java.util.Date;

public class SaveSupplierViewModel  extends IntranetVM {
    private ProductSupplier selectedProductSupplier;
    private InventoryService inventoryService;

    @AfterCompose
    public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view,
                               @ExecutionArgParam("productSupplierToModify") ProductSupplier productSupplier) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        inventoryService = context.getBean(InventoryService.class);
        if(productSupplier != null){
            this.selectedProductSupplier = productSupplier;
        }else{
            this.selectedProductSupplier = new ProductSupplier();
        }
        addCommonTags((PageCtrl) view.getPage());
    }

    @Command
    public void saveProductSupplier() {
        try {
            selectedProductSupplier.setCreateDate(new Timestamp(new Date().getTime()));
            inventoryService.saveProductSupplier(selectedProductSupplier);
            Messagebox.show("OK! Do you want to view all the Product Supplier?", "Warning", Messagebox.OK | Messagebox.CANCEL, Messagebox.INFORMATION, new EventListener<Event>() {
                public void onEvent(Event event) throws Exception {
                    if (event.getName().equals("onOK")) {
                        Executions.sendRedirect("/pages/inventory/supplier/viewSupplier.zul");
                    }
                }
            });
        }catch (PersistenceException persistenceException){
            Messagebox.show("Product Supplier with same name already exits", "Warning", Messagebox.OK | Messagebox.CANCEL, Messagebox.INFORMATION);
        }
    }

    public ProductSupplier getSelectedProductSupplier() {
        return selectedProductSupplier;
    }

    public void setSelectedProductSupplier(ProductSupplier selectedProductSupplier) {
        this.selectedProductSupplier = selectedProductSupplier;
    }

    public InventoryService getInventoryService() {
        return inventoryService;
    }

    public void setInventoryService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }


}
