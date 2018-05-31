package qa.tecnositafgulf.viewmodel.inventory.productLocation;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.sys.PageCtrl;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;
import qa.tecnositafgulf.model.inventory.location.Location;
import qa.tecnositafgulf.model.inventory.product.Product;
import qa.tecnositafgulf.model.inventory.stocks.ProductStock;
import qa.tecnositafgulf.searchcriteria.inventory.ProductStockSearchCriteria;
import qa.tecnositafgulf.service.InventoryService;
import qa.tecnositafgulf.spring.config.AppConfig;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewProductLocationViewModel  extends IntranetVM {

    private List<ProductStock> productStockList;
    private InventoryService inventoryService;
    private Location location;
    private Product product;
    private ProductStockSearchCriteria productStockSearchCriteria;
    private Integer totalSize;
    private Integer activePage;

    @AfterCompose
    public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        inventoryService = context.getBean(InventoryService.class);
        productStockSearchCriteria = new ProductStockSearchCriteria();
        this.setActivePage(this.productStockSearchCriteria.getStartIndex());
        loadData();
        addCommonTags((PageCtrl) view.getPage());
    }

    private void loadData() {
        this.setTotalSize(this.inventoryService.getAllProductsProductStockCount(productStockSearchCriteria));
        this.productStockSearchCriteria.setStartIndex(getActivePage());
        productStockList = inventoryService.getAllProductsProductStocks(productStockSearchCriteria);
    }

    @Command
    @NotifyChange({"activePage", "totalSize", "productStockSearchCriteria", "productStockList"})
    public void order(@BindingParam("orderBy") String orderBy) {
        this.productStockSearchCriteria.setOrderByFieldName(orderBy);
        this.productStockSearchCriteria.setPageOrderMode("asc".equalsIgnoreCase(this.productStockSearchCriteria.getPageOrderMode()) ? "desc" : "asc");
        //load method call to load data
        loadData();
    }

    @Command
    @NotifyChange({"activePage", "totalSize", "productStockSearchCriteria", "productStockList"})
    public void clearFilters() {
        this.productStockSearchCriteria.clear();
        this.productStockSearchCriteria.setPageOrderMode("desc");
        this.productStockSearchCriteria.clearFilters();
        loadData();
    }

    @Command
    @NotifyChange({"activePage", "totalSize", "locationSearchCriteria", "productStockList"})
    public void filter() {
        this.productStockSearchCriteria.clear();
        this.productStockSearchCriteria.setPageOrderMode("desc");
        this.activePage = this.productStockSearchCriteria.getStartIndex();
        loadData();
    }

    @Command
    @NotifyChange("productStockList")
    public void add() {
        final Map<String, ProductStock> params = new HashMap<String, ProductStock>();
        params.put("productStockToModify", null);
        ((Window) Executions.getCurrent().createComponents("/pages/inventory/productLocation/saveProductLocation.zul", null, params)).doModal();
    }

    @Command
    @NotifyChange("productStockList")
    public void modify(@BindingParam("item") ProductStock productStock) {
        final Map<String, ProductStock> params = new HashMap<String, ProductStock>();
        params.put("productStockToModify", productStock);
        ((Window) Executions.getCurrent().createComponents("/pages/inventory/productLocation/saveProductLocation.zul", null, params)).doModal();
    }

    @Command
    @NotifyChange("productStockList")
    public void delete(@BindingParam("item") final ProductStock productStock) {
        Messagebox.show("Do you want to delete this product Stock?", "Warning", Messagebox.OK | Messagebox.CANCEL, Messagebox.INFORMATION, new EventListener<Event>() {
            public void onEvent(Event event) throws Exception {
                if (event.getName().equals("onOK")) {
                    try {
                        productStockList.remove(productStock);
                        inventoryService.remove(productStock);
                        Executions.sendRedirect("/pages/inventory/productLocation/viewProductLocation.zul");
                    } catch (Exception exception) {
                        Messagebox.show("Cannot delete Stock", "INFORMATION", Messagebox.OK, Messagebox.INFORMATION);
                    }
                }
            }
        });
    }

    public List<ProductStock> getProductStockList() {
        return productStockList;
    }

    public void setProductStockList(List<ProductStock> productStockList) {
        this.productStockList = productStockList;
    }

    public InventoryService getInventoryService() {
        return inventoryService;
    }

    public void setInventoryService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ProductStockSearchCriteria getProductStockSearchCriteria() {
        return productStockSearchCriteria;
    }

    public void setProductStockSearchCriteria(ProductStockSearchCriteria productStockSearchCriteria) {
        this.productStockSearchCriteria = productStockSearchCriteria;
    }

    public Integer getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Integer totalSize) {
        this.totalSize = totalSize;
    }

    public Integer getActivePage() {
        return activePage;
    }

    @NotifyChange({"activePage", "totalSize", "locationSearchCriteria", "productStockList"})
    public void setActivePage(Integer activePage) {
        this.activePage = activePage;
        loadData();
    }
}
