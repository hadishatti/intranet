package qa.tecnositafgulf.viewmodel.inventory.supplier;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.sys.PageCtrl;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;
import qa.tecnositafgulf.model.inventory.supplier.ProductSupplier;
import qa.tecnositafgulf.searchcriteria.inventory.ProductSupplierSearchCriteria;
import qa.tecnositafgulf.service.InventoryService;
import qa.tecnositafgulf.spring.config.AppConfig;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ameljo on 5/29/18.
 */
public class ProductSupplierViewModel extends IntranetVM {
    private List<ProductSupplier> productSupplierList;
    private InventoryService inventoryService;
    private ProductSupplier  supplier;
    private ProductSupplierSearchCriteria productSupplierSearchCriteria;
    private Integer totalSize;
    private Integer activePage;

    @AfterCompose
    public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        inventoryService = context.getBean(InventoryService.class);
        productSupplierSearchCriteria = new ProductSupplierSearchCriteria();
        this.setActivePage(this.productSupplierSearchCriteria.getStartIndex());
        loadData();
        addCommonTags((PageCtrl) view.getPage());
    }

    private void loadData() {
        this.setTotalSize(this.inventoryService.getProductSuppliersCount(productSupplierSearchCriteria));
        this.productSupplierSearchCriteria.setStartIndex(getActivePage());
        productSupplierList = inventoryService.getProductSuppliers(productSupplierSearchCriteria);
    }

    @Command
    @NotifyChange({"activePage", "totalSize", "productSupplierSearchCriteria", "productSupplierList"})
    public void order(@BindingParam("orderBy") String orderBy) {
        this.productSupplierSearchCriteria.setOrderByFieldName(orderBy);
        this.productSupplierSearchCriteria.setPageOrderMode("asc".equalsIgnoreCase(this.productSupplierSearchCriteria.getPageOrderMode()) ? "desc" : "asc");
        //load method call to load data
        loadData();
    }

    @Command
    @NotifyChange({"activePage", "totalSize", "productSupplierSearchCriteria", "productSupplierList"})
    public void clearFilters() {
        this.productSupplierSearchCriteria.clear();
        this.productSupplierSearchCriteria.setPageOrderMode("desc");
        this.productSupplierSearchCriteria.clearFilters();
        loadData();
    }

    @Command
    @NotifyChange({"activePage", "totalSize", "productSupplierSearchCriteria", "productSupplierList"})
    public void filter() {
        this.productSupplierSearchCriteria.clear();
        this.productSupplierSearchCriteria.setPageOrderMode("desc");
        this.activePage = this.productSupplierSearchCriteria.getStartIndex();
        loadData();
    }

    @Command
    @NotifyChange("productSupplierList")
    public void add() {
        final Map<String, ProductSupplier> params = new HashMap<String, ProductSupplier>();
        params.put("productSupplierToModify", null);
        ((Window) Executions.getCurrent().createComponents("/pages/inventory/supplier/saveSupplier.zul", null, params)).doModal();
    }

    @Command
    @NotifyChange("productSupplierList")
    public void modify(@BindingParam("item") ProductSupplier productSupplier) {
        final Map<String, ProductSupplier> params = new HashMap<String, ProductSupplier>();
        params.put("productSupplierToModify", productSupplier);
        ((Window) Executions.getCurrent().createComponents("/pages/inventory/supplier/saveSupplier.zul", null, params)).doModal();
    }

    @Command
    @NotifyChange("productSupplierList")
    public void delete(@BindingParam("item") final ProductSupplier productSupplier) {

        Messagebox.show("Do you want to delete this product Supplier?", "Warning", Messagebox.OK | Messagebox.CANCEL, Messagebox.INFORMATION, new EventListener<Event>() {
            public void onEvent(Event event) throws Exception {
                if (event.getName().equals("onOK")) {
                    try {
                        productSupplierList.remove(productSupplier);
                        inventoryService.removeProductSupplier(productSupplier);
                        Executions.sendRedirect("/pages/inventory/supplier/viewSuppliers.zul");
                    } catch (Exception exception) {
                        Messagebox.show("Cannot delete Supplier", "INFORMATION", Messagebox.OK, Messagebox.INFORMATION);
                    }
                }
            }
        });
    }

    public ProductSupplier getSupplier() {
        return supplier;
    }

    public void setSupplier(ProductSupplier supplier) {
        this.supplier = supplier;
    }

    public ProductSupplierSearchCriteria getProductSupplierSearchCriteria() {
        return productSupplierSearchCriteria;
    }

    public void setProductSupplierSearchCriteria(ProductSupplierSearchCriteria productSupplierSearchCriteria) {
        this.productSupplierSearchCriteria = productSupplierSearchCriteria;
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

    public void setActivePage(Integer activePage) {
        this.activePage = activePage;
    }

    public List<ProductSupplier> getProductSupplierList() {
        return productSupplierList;
    }

    public void setProductSupplierList(List<ProductSupplier> productSupplierList) {
        this.productSupplierList = productSupplierList;
    }

    public InventoryService getInventoryService() {
        return inventoryService;
    }

    public void setInventoryService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }


}
