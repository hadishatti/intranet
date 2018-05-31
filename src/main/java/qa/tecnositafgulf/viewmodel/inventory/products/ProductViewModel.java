package qa.tecnositafgulf.viewmodel.inventory.products;

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
import qa.tecnositafgulf.searchcriteria.inventory.ProductSearchCriteria;
import qa.tecnositafgulf.service.InventoryService;
import qa.tecnositafgulf.spring.config.AppConfig;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import java.util.*;

public class ProductViewModel  extends IntranetVM {

    private List<Product> productList;
    private List<Location> locationList;
    private InventoryService inventoryService;
    private ProductSearchCriteria productSearchCriteria;
    private Integer totalSize;
    private Integer activePage;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        inventoryService = context.getBean(InventoryService.class);
        productSearchCriteria = new ProductSearchCriteria();
        this.setActivePage(this.productSearchCriteria.getStartIndex());
        loadData();
        addCommonTags((PageCtrl) view.getPage());
    }

    private void loadData(){
        this.setTotalSize(this.inventoryService.getAllProductCount(productSearchCriteria));
        this.productSearchCriteria.setStartIndex(getActivePage());
        productList = inventoryService.getAllProducts(productSearchCriteria);
        if(productList != null) {
            locationList = getLocations();
        }
    }

    @Command
    @NotifyChange({"activePage", "totalSize", "productSearchCriteria", "productList"})
    public void order(@BindingParam("orderBy") String orderBy){
        this.productSearchCriteria.setOrderByFieldName(orderBy);
        this.productSearchCriteria.setPageOrderMode("asc".equalsIgnoreCase(this.productSearchCriteria.getPageOrderMode()) ? "desc" : "asc");
        //load method call to load data
        loadData();
    }

    @Command
    @NotifyChange({"activePage", "totalSize", "productSearchCriteria", "productList"})
    public void clearFilters() {
        this.productSearchCriteria.clear();
        this.productSearchCriteria.setPageOrderMode("desc");
        this.productSearchCriteria.clearFilters();
        loadData();
    }

    @Command
    @NotifyChange({"activePage", "totalSize", "productSearchCriteria", "productList"})
    public void filter() {
        this.productSearchCriteria.clear();
        this.productSearchCriteria.setPageOrderMode("desc");
        this.activePage = this.productSearchCriteria.getStartIndex();
        loadData();
    }

    @Command
    @NotifyChange("productList")
    public void add(){
        final Map<String, Product> params = new HashMap<String, Product>();
        params.put("productToModify", null);
        ((Window) Executions.getCurrent().createComponents("/pages/inventory/product/saveProduct.zul",null, params)).doModal();
    }

    @Command
    @NotifyChange("productList")
    public void modify(@BindingParam("item") Product product){
        final Map<String, Product> params = new HashMap<String, Product>();
        params.put("productToModify", product);
        ((Window) Executions.getCurrent().createComponents("/pages/inventory/product/saveProduct.zul", null, params)).doModal();
    }

    @Command
    @NotifyChange("productList")
    public void delete(@BindingParam("item") final Product product){
        Messagebox.show("Do you want to delete this product?", "Warning", Messagebox.OK | Messagebox.CANCEL, Messagebox.INFORMATION, new EventListener<Event>() {
            public void onEvent(Event event) throws Exception {
                if (event.getName().equals("onOK")) {
                    try {
                        productList.remove(product);
                        inventoryService.remove(product);
                        loadData();
                        Executions.sendRedirect("/pages/inventory/product/viewProducts.zul");
                    }catch (Exception exception){
                        Messagebox.show("Cannot delete this Product atleast one stock attached", "Information", Messagebox.OK, Messagebox.INFORMATION);
                    }
                }
            }
        });
    }

    private List<Location> getLocations(){
        List<Location> locations = new ArrayList<>();
        for (Iterator<Product> locationIterator = productList.iterator(); locationIterator.hasNext();){
            locations.addAll(locationIterator.next().getLocationList());
        }
        return locations;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public InventoryService getInventoryService() {
        return inventoryService;
    }

    public void setInventoryService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public ProductSearchCriteria getProductSearchCriteria() {
        return productSearchCriteria;
    }

    public void setProductSearchCriteria(ProductSearchCriteria productSearchCriteria) {
        this.productSearchCriteria = productSearchCriteria;
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

    @NotifyChange({"activePage", "totalSize", "productSearchCriteria", "productList"})
    public void setActivePage(Integer activePage) {
        this.activePage = activePage;
        loadData();
    }

    public List<Location> getLocationList() {
        return locationList;
    }

    public void setLocationList(List<Location> locationList) {
        this.locationList = locationList;
    }

}
