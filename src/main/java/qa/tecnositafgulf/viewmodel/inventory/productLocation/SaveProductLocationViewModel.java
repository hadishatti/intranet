package qa.tecnositafgulf.viewmodel.inventory.productLocation;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Messagebox;
import qa.tecnositafgulf.model.inventory.location.Location;
import qa.tecnositafgulf.model.inventory.product.Product;
import qa.tecnositafgulf.model.inventory.stocks.ProductLocation;
import qa.tecnositafgulf.model.inventory.stocks.ProductStock;
import qa.tecnositafgulf.service.InventoryService;
import qa.tecnositafgulf.spring.config.AppConfig;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import javax.persistence.PersistenceException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SaveProductLocationViewModel  extends IntranetVM {
    private List<Location> locationList;
    private List<Product> productList;
    private ProductStock productStock;
    private Product selectedProduct;
    private Location selectedLocation;
    private InventoryService inventoryService;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view,
                             @ExecutionArgParam("productStockToModify") ProductStock productStock) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        inventoryService = context.getBean(InventoryService.class);
        locationList = inventoryService.getAllLocations();
        productList = inventoryService.getAllProducts();
        if(productStock != null) {
            this.productStock = productStock;
            this.selectedProduct = this.productStock.getProduct();
            this.selectedLocation = this.productStock.getLocation();
        }else {
            this.productStock = new ProductStock();
        }
    }

    @Command
    @NotifyChange({"productStock"})
    public void getProductDetails(){
        if(selectedProduct !=null && selectedLocation != null){
            productStock = this.inventoryService.getProductStockOnProductLocation(selectedLocation, selectedProduct);//get from dataBase against product and location
            if(productStock == null)
                this.productStock = new ProductStock();
        }

    }
    @Command
    @NotifyChange({"productStock"})
    public void getProductStockDetails(){
        if(selectedProduct !=null && selectedLocation != null){
            productStock = this.inventoryService.getProductStockOnProductLocation(selectedLocation, selectedProduct);//get from dataBase against product and location
            if(productStock == null)
                this.productStock = new ProductStock();
        }
    }


    @Command
    public void saveProductLocation() {
        try {
            List<Location> locations = new ArrayList<>();
            List<Product> products = new ArrayList<>();
            products.add(selectedProduct);
            locations.add(selectedLocation);
            ProductLocation productLocation = new ProductLocation();
            productLocation.setLocationId(selectedLocation.getId());
            productLocation.setProductId(selectedProduct.getId());
            productStock.setProductLocation(productLocation);
            productStock.setCreateDate(new Timestamp(new Date().getTime()));
            productStock.setLocation(selectedLocation);
            productStock.setProduct(selectedProduct);
            selectedProduct.setLocationList(locations);
            selectedLocation.setProductList(products);
            inventoryService.save(productStock);
            Messagebox.show("OK! Do you want to view all the Product Stock?", "Warning", Messagebox.OK | Messagebox.CANCEL, Messagebox.INFORMATION, new EventListener<Event>() {
                public void onEvent(Event event) throws Exception {
                    if (event.getName().equals("onOK")) {
                        Executions.sendRedirect("/pages/inventory/productLocation/viewProductLocation.zul");
                    }
                }
            });
        }catch (PersistenceException persistenceException){
            Messagebox.show("Product with same name already exits", "Warning", Messagebox.OK | Messagebox.CANCEL, Messagebox.INFORMATION);
        }
    }

    public List<Location> getLocationList() {
        return locationList;
    }

    public void setLocationList(List<Location> locationList) {
        this.locationList = locationList;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public ProductStock getProductStock() {
        return productStock;
    }

    public void setProductStock(ProductStock productStock) {
        this.productStock = productStock;
    }

    public Product getSelectedProduct() {
        return selectedProduct;
    }

    public void setSelectedProduct(Product selectedProduct) {
        this.selectedProduct = selectedProduct;
    }

    public Location getSelectedLocation() {
        return selectedLocation;
    }

    public void setSelectedLocation(Location selectedLocation) {
        this.selectedLocation = selectedLocation;
    }

    public InventoryService getInventoryService() {
        return inventoryService;
    }

    public void setInventoryService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }
}
