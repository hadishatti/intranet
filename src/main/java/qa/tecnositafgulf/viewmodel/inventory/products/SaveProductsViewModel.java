package qa.tecnositafgulf.viewmodel.inventory.products;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Messagebox;
import qa.tecnositafgulf.model.inventory.location.Location;
import qa.tecnositafgulf.model.inventory.product.Product;
import qa.tecnositafgulf.model.inventory.productCategory.ProductCategory;
import qa.tecnositafgulf.model.inventory.stocks.ProductStock;
import qa.tecnositafgulf.service.InventoryService;
import qa.tecnositafgulf.spring.config.AppConfig;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SaveProductsViewModel  extends IntranetVM {

    public static final String OFFIC_LOC = "Head Office";
    private InventoryService inventoryService;
    private List<ProductCategory> productCategoryList;
    private List<Location> locationList;
    private ProductCategory selectedProductCategory;
    private Location selectedLocation;
    private Product product;
    private ProductStock productStock;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view,
                             @ExecutionArgParam("productToModify") Product product) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        inventoryService = context.getBean(InventoryService.class);
        this.productCategoryList = inventoryService.getAllProductCategories();
        locationList = inventoryService.getAllLocations();
        if(product != null) {
            this.product = product;
            selectedProductCategory = this.product.getProductCategory();
            locationList = this.product.getLocationList(); //get from database;
        }else {
            this.product = new Product();
        }
    }
    @Command
    @NotifyChange({"productStock"})
    public void onChangeLocation(@ExecutionArgParam("location") Location location,
                                 @ExecutionArgParam("product") Product product){
        productStock = inventoryService.getProductStockLocationWise(product, location);
        if(productStock == null)
            productStock = new ProductStock();
    }

    @Command
    @NotifyChange("productList")
    public void saveProduct() {
        try {
            List<Location> locations = new ArrayList<>();
            selectedLocation = inventoryService.getLocationByName(OFFIC_LOC);//get default location from DB
            locations.add(selectedLocation);
            product.setCreateDate(new Timestamp(new Date().getTime()));
            product.setProductCategory(selectedProductCategory);
            product.setLocationList(locations);
            inventoryService.save(product);
            Messagebox.show("OK! Do you want to view all the Product?", "Warning", Messagebox.OK | Messagebox.CANCEL, Messagebox.INFORMATION, new EventListener<Event>() {
                public void onEvent(Event event) throws Exception {
                    if (event.getName().equals("onOK")) {
                        Executions.sendRedirect("/pages/inventory/product/viewProducts.zul");
                    }
                }
            });
        }catch (PersistenceException persistenceException){
                if(persistenceException instanceof  NoResultException){
                    Messagebox.show("Default Location not exits, kindly create location as Head Office", "Warning", Messagebox.OK | Messagebox.CANCEL, Messagebox.INFORMATION);
                }else {
                    Messagebox.show("Product with same name already exits", "Warning", Messagebox.OK | Messagebox.CANCEL, Messagebox.INFORMATION);
                }
        }
    }



    public InventoryService getInventoryService() {
        return inventoryService;
    }

    public void setInventoryService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<ProductCategory> getProductCategoryList() {
        return productCategoryList;
    }

    public void setProductCategoryList(List<ProductCategory> productCategoryList) {
        this.productCategoryList = productCategoryList;
    }

    public ProductCategory getSelectedProductCategory() {
        return selectedProductCategory;
    }

    public void setSelectedProductCategory(ProductCategory selectedProductCategory) {
        this.selectedProductCategory = selectedProductCategory;
    }

    public List<Location> getLocationList() {
        return locationList;
    }

    public void setLocationList(List<Location> locationList) {
        this.locationList = locationList;
    }

    public ProductStock getProductStock() {
        return productStock;
    }

    public void setProductStock(ProductStock productStock) {
        this.productStock = productStock;
    }

    public Location getSelectedLocation() {
        return selectedLocation;
    }

    public void setSelectedLocation(Location selectedLocation) {
        this.selectedLocation = selectedLocation;
    }
}
