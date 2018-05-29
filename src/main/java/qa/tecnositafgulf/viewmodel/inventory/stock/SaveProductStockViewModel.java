package qa.tecnositafgulf.viewmodel.inventory.stock;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Messagebox;
import qa.tecnositafgulf.model.inventory.location.Location;
import qa.tecnositafgulf.model.inventory.product.Product;
import qa.tecnositafgulf.model.inventory.stocks.ProductStock;
import qa.tecnositafgulf.service.InventoryService;
import qa.tecnositafgulf.spring.config.AppConfig;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import javax.persistence.PersistenceException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class SaveProductStockViewModel  extends IntranetVM {

    private ProductStock productStock;
    private List<Product> productList;
    private List<Location> locationList;
    private Product selectedProduct;
    private Location selectedLocation;
    private InventoryService inventoryService;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view,
                             @ExecutionArgParam("productStockToModify") ProductStock productStock) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        inventoryService = context.getBean(InventoryService.class);
        this.locationList = inventoryService.getAllLocations();
        this.productList = inventoryService.getAllProducts();
        if(productStock != null) {
            this.productStock = productStock;
        }else {
            this.productStock = new ProductStock();
        }
        //selectedProduct = this.productStock.getProduct();
       // selectedLocation = this.productStock.getLocation();
    }

    @Command
    @NotifyChange("productStock")
    public void onChangeProduct(@BindingParam("selectedIndex") Integer selectedIndex, @BindingParam("selectedProduct") Product product){

        System.out.println("Selected Product = "+product);

    }

    @Command
    public void back(){
        Executions.sendRedirect("/pages/inventory/stock/viewProductStock.zul");
    }

    @Command
    @NotifyChange("productStockList")
    public void saveProductStock() {
        try {
            productStock.setCreateDate(new Timestamp(new Date().getTime()));
            //productStock.setProduct(selectedProduct);
            //productStock.setLocation(selectedLocation);
            inventoryService.save(productStock);
            Messagebox.show("OK! Do you want to view all the Product Stock?", "Warning", Messagebox.OK | Messagebox.CANCEL, Messagebox.INFORMATION, new EventListener<Event>() {
                public void onEvent(Event event) throws Exception {
                    if (event.getName().equals("onOK")) {
                        Executions.sendRedirect("/pages/inventory/stock/viewProductStock.zul");
                    }
                }
            });
        }catch (PersistenceException persistenceException){
            Messagebox.show("Product stock with same name already exits", "Warning", Messagebox.OK | Messagebox.CANCEL, Messagebox.INFORMATION);
        }
    }

    public ProductStock getProductStock() {
        return productStock;
    }

    public void setProductStock(ProductStock productStock) {
        this.productStock = productStock;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public Product getSelectedProduct() {
        return selectedProduct;
    }

    public void setSelectedProduct(Product selectedProduct) {
        this.selectedProduct = selectedProduct;
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

    public Location getSelectedLocation() {
        return selectedLocation;
    }

    public void setSelectedLocation(Location selectedLocation) {
        this.selectedLocation = selectedLocation;
    }
}
