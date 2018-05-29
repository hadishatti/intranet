package qa.tecnositafgulf.viewmodel.inventory.productCategory;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Messagebox;
import qa.tecnositafgulf.model.inventory.productCategory.ProductCategory;
import qa.tecnositafgulf.service.InventoryService;
import qa.tecnositafgulf.spring.config.AppConfig;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import javax.persistence.PersistenceException;
import java.sql.Timestamp;
import java.util.Date;

public class SaveProductsCategoryViewModel  extends IntranetVM {

    private ProductCategory productCategory;
    private InventoryService inventoryService;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view,
                             @ExecutionArgParam("productCategoryToModify") ProductCategory productCategory) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        inventoryService = context.getBean(InventoryService.class);
        if(productCategory != null)
           this.productCategory = productCategory;
        else
            this.productCategory = new ProductCategory();
    }

    @Command
    @NotifyChange("productCategoryList")
    public void saveProductCategory() {
        try {
            productCategory.setCreatedDate(new Timestamp(new Date().getTime()));
            inventoryService.save(productCategory);
            Messagebox.show("OK! Do you want to view all the Product Category?", "Warning", Messagebox.OK | Messagebox.CANCEL, Messagebox.INFORMATION, new EventListener<Event>() {
                public void onEvent(Event event) throws Exception {
                    if (event.getName().equals("onOK")) {
                        Executions.sendRedirect("/pages/inventory/productCategory/viewProductsCategory.zul");
                    }
                }
            });
        }catch (PersistenceException persistenceException){
            Messagebox.show("Product category with same name already exits", "Warning", Messagebox.OK | Messagebox.CANCEL, Messagebox.INFORMATION);
        }
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    public InventoryService getInventoryService() {
        return inventoryService;
    }

    public void setInventoryService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }
}
