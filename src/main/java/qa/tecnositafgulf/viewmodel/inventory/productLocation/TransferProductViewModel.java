package qa.tecnositafgulf.viewmodel.inventory.productLocation;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import qa.tecnositafgulf.model.inventory.location.Location;
import qa.tecnositafgulf.model.inventory.product.Product;
import qa.tecnositafgulf.model.inventory.stocks.ProductStock;
import qa.tecnositafgulf.model.inventory.stocks.TransferStock;
import qa.tecnositafgulf.service.InventoryService;
import qa.tecnositafgulf.spring.config.AppConfig;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransferProductViewModel  extends IntranetVM {

    private List<Location> fromLocationList;
    private List<Location> toLocationList;

    private ListModelList<Product> productList;

    private Product selectedProduct;
    private Location fromSelectedLocation;
    private Location toSelectedLocation;

    private Long fromCurrentStock;
    private Long toCurrentStock;
    private Long updatedToStock;

    private InventoryService inventoryService;
    private ProductStock fromProductStock;
    private ProductStock toProductStock;
    private TransferStock transferStock;

    @AfterCompose
    public void doAfterCompose() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        inventoryService = context.getBean(InventoryService.class);

        fromLocationList = inventoryService.getAllLocations();
        toLocationList = inventoryService.getAllLocations();
        productList = new ListModelList(new ArrayList());
        this.transferStock = new TransferStock();

    }

    @Command
    @NotifyChange({"productList", "selectedProduct","toProductStock", "fromProductStock"})
    public void getProductInfo() {
        if (fromSelectedLocation != null) {
            productList.clear();
            toProductStock = null;
            selectedProduct = null;
            fromProductStock = null;
            productList.addAll(inventoryService.getProductStockOnLocation(fromSelectedLocation));//get the product list against product location
        }
    }

    @Command
    @NotifyChange({"fromProductStock", "toProductStock"})
    public void getFromProductStockDetails() {
        fromProductStock = null;
        toProductStock = null;
        if (selectedProduct != null && fromSelectedLocation != null) {
            fromProductStock = this.inventoryService.getProductStockOnProductLocation(fromSelectedLocation, selectedProduct);//get from dataBase against product and location
            if (toSelectedLocation != null)
                toProductStock = this.inventoryService.getProductStockOnProductLocation(toSelectedLocation, selectedProduct);//get from dataBase against product and location

        }
    }

    @Command
    @NotifyChange({"toProductStock"})
    public void getToProductStockDetails() {
        toProductStock = null;
        if (selectedProduct != null && toSelectedLocation != null) {
            toProductStock = this.inventoryService.getProductStockOnProductLocation(toSelectedLocation, selectedProduct);//get from dataBase against product and location
            if(toProductStock == null)
                toProductStock = new ProductStock();
        }
    }

    @Command
    public void saveProductTransfer(){
        if(fromSelectedLocation.equals(toSelectedLocation)){
            Messagebox.show("Cannot transfer within location", "Warning", Messagebox.OK | Messagebox.CANCEL, Messagebox.INFORMATION);
            return;
        }
        if(fromProductStock.getCurrentStock() < updatedToStock){
            Messagebox.show("No. of stocks cannot be greater than current stocks", "Warning", Messagebox.OK | Messagebox.CANCEL, Messagebox.INFORMATION);
            return;
        }

        fromProductStock.setCurrentStock(fromProductStock.getCurrentStock() - updatedToStock);
        toProductStock.setCurrentStock((fromProductStock.getCurrentStock() == null? 0: fromProductStock.getCurrentStock() )+ updatedToStock);
        inventoryService.updateProductStocks(fromProductStock, toProductStock);

        transferStock.setCreateDate(new Timestamp(new Date().getTime()));
        transferStock.setFromLocationName(fromProductStock.getLocation().getName());
        transferStock.setToLocationName(toProductStock.getLocation().getName());
        transferStock.setProductName(selectedProduct.getProductName());
        transferStock.setNoOfStocksTransfer(updatedToStock);

        inventoryService.addStockTransferDetails(transferStock);
        Executions.sendRedirect("/pages/inventory/productLocation/viewProductLocation.zul");

    }


    public Product getSelectedProduct() {
        return selectedProduct;
    }

    public void setSelectedProduct(Product selectedProduct) {
        this.selectedProduct = selectedProduct;
    }

    public TransferStock getTransferStock() {
        return transferStock;
    }

    public void setTransferStock(TransferStock transferStock) {
        this.transferStock = transferStock;
    }

    public InventoryService getInventoryService() {
        return inventoryService;
    }

    public void setInventoryService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }


    public List<Location> getFromLocationList() {
        return fromLocationList;
    }

    public void setFromLocationList(List<Location> fromLocationList) {
        this.fromLocationList = fromLocationList;
    }


    public Location getFromSelectedLocation() {
        return fromSelectedLocation;
    }

    public void setFromSelectedLocation(Location fromSelectedLocation) {
        this.fromSelectedLocation = fromSelectedLocation;
    }

    public List<Location> getToLocationList() {
        return toLocationList;
    }

    public void setToLocationList(List<Location> toLocationList) {
        this.toLocationList = toLocationList;
    }

    public Location getToSelectedLocation() {
        return toSelectedLocation;
    }

    public void setToSelectedLocation(Location toSelectedLocation) {
        this.toSelectedLocation = toSelectedLocation;
    }

    public Long getFromCurrentStock() {
        return fromCurrentStock;
    }

    public void setFromCurrentStock(Long fromCurrentStock) {
        this.fromCurrentStock = fromCurrentStock;
    }

    public Long getToCurrentStock() {
        return toCurrentStock;
    }

    public void setToCurrentStock(Long toCurrentStock) {
        this.toCurrentStock = toCurrentStock;
    }

    public Long getUpdatedToStock() {
        return updatedToStock;
    }

    public void setUpdatedToStock(Long updatedToStock) {
        this.updatedToStock = updatedToStock;
    }

    public ProductStock getFromProductStock() {
        return fromProductStock;
    }

    public void setFromProductStock(ProductStock fromProductStock) {
        this.fromProductStock = fromProductStock;
    }

    public ProductStock getToProductStock() {
        return toProductStock;
    }

    public void setToProductStock(ProductStock toProductStock) {
        this.toProductStock = toProductStock;
    }

    public ListModelList<Product> getProductList() {
        return productList;
    }

    public void setProductList(ListModelList<Product> productList) {
        this.productList = productList;
    }


}
