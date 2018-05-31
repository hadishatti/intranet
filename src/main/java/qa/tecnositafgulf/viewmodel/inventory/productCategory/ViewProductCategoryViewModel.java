package qa.tecnositafgulf.viewmodel.inventory.productCategory;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.sys.PageCtrl;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;
import qa.tecnositafgulf.model.inventory.productCategory.ProductCategory;
import qa.tecnositafgulf.searchcriteria.inventory.ProductCategorySearchCriteria;
import qa.tecnositafgulf.service.InventoryService;
import qa.tecnositafgulf.spring.config.AppConfig;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewProductCategoryViewModel  extends IntranetVM {

    private List<ProductCategory> productCategoryList;
    private InventoryService inventoryService;
    private ProductCategorySearchCriteria productCategorySearchCriteria;
    private Integer totalSize;
    private Integer activePage;

    @AfterCompose
    public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        inventoryService = context.getBean(InventoryService.class);
        productCategorySearchCriteria = new ProductCategorySearchCriteria();
        this.setActivePage(this.productCategorySearchCriteria.getStartIndex());
        loadData();
        addCommonTags((PageCtrl) view.getPage());
    }

    private void loadData(){
        this.setTotalSize(this.inventoryService.getProductCategoryCount(productCategorySearchCriteria));
        this.productCategorySearchCriteria.setStartIndex(getActivePage());
        productCategoryList = inventoryService.getAllProductCategories(productCategorySearchCriteria);
    }

    @Command
    @NotifyChange({"activePage", "totalSize", "productCategorySearchCriteria", "productCategoryList"})
    public void order(@BindingParam("orderBy") String orderBy){
        this.productCategorySearchCriteria.setOrderByFieldName(orderBy);
        this.productCategorySearchCriteria.setPageOrderMode("asc".equalsIgnoreCase(this.productCategorySearchCriteria.getPageOrderMode()) ? "desc" : "asc");
        //load method call to load data
        loadData();
    }

    @Command
    @NotifyChange({"activePage", "totalSize", "productCategorySearchCriteria", "productCategoryList"})
    public void clearFilters() {
        this.productCategorySearchCriteria.clear();
        this.productCategorySearchCriteria.setPageOrderMode("desc");
        this.productCategorySearchCriteria.clearFilters();
        loadData();
    }

    @Command
    @NotifyChange({"activePage", "totalSize", "productCategorySearchCriteria", "productCategoryList"})
    public void filter() {
        this.productCategorySearchCriteria.clear();
        this.productCategorySearchCriteria.setPageOrderMode("desc");
        this.activePage = this.productCategorySearchCriteria.getStartIndex();
        loadData();
    }

    @Command
    @NotifyChange("productCategoryList")
    public void add() {
        final Map<String, ProductCategory> params = new HashMap<String, ProductCategory>();
        params.put("productCategoryToModify", null);
        ((Window) Executions.getCurrent().createComponents("/pages/inventory/productCategory/saveProductsCategory.zul", null, params)).doModal();
    }

    @Command
    @NotifyChange("productCategoryList")
    public void modify(@BindingParam("item") ProductCategory productCategory) {
        final Map<String, ProductCategory> params = new HashMap<String, ProductCategory>();
        params.put("productCategoryToModify", productCategory);
        ((Window) Executions.getCurrent().createComponents("/pages/inventory/productCategory/saveProductsCategory.zul", null, params)).doModal();
    }

    @Command
    @NotifyChange("productCategoryList")
    public void delete(@BindingParam("item") final ProductCategory productCategory) {
        Messagebox.show("Do you want to delete this product Category?", "Warning", Messagebox.OK | Messagebox.CANCEL, Messagebox.INFORMATION, new EventListener<Event>() {
            public void onEvent(Event event) throws Exception {
                if (event.getName().equals("onOK")) {
                    try {
                        inventoryService.remove(productCategory);
                        productCategoryList.remove(productCategory);
                        loadData();
                        Executions.sendRedirect("/pages/inventory/productCategory/viewProductsCategory.zul");
                    } catch (Exception exception) {
                        Messagebox.show("Cannot delete the product category beacuse it's attached to atleast one product", "Information", Messagebox.OK, Messagebox.INFORMATION);
                    }
                }
            }
        });
    }

    public List<ProductCategory> getProductCategoryList() {
        return productCategoryList;
    }

    public void setProductCategoryList(List<ProductCategory> productCategoryList) {
        this.productCategoryList = productCategoryList;
    }

    public InventoryService getInventoryService() {
        return inventoryService;
    }

    public void setInventoryService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public ProductCategorySearchCriteria getProductCategorySearchCriteria() {
        return productCategorySearchCriteria;
    }

    public void setProductCategorySearchCriteria(ProductCategorySearchCriteria productCategorySearchCriteria) {
        this.productCategorySearchCriteria = productCategorySearchCriteria;
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

    @NotifyChange({"activePage", "totalSize", "productCategorySearchCriteria", "productCategoryList"})
    public void setActivePage(Integer activePage) {
        this.activePage = activePage;
        loadData();
    }
}
