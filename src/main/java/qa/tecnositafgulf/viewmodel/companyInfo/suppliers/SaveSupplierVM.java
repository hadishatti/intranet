package qa.tecnositafgulf.viewmodel.companyInfo.suppliers;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import qa.tecnositafgulf.model.suppliers.Supplier;
import qa.tecnositafgulf.service.SupplierService;
import qa.tecnositafgulf.spring.config.AppConfig;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import java.io.IOException;

/**
 * Created by ledio on 5/7/18.
 */
public class SaveSupplierVM extends IntranetVM{

    private Supplier supplier;
    private String name;
    private String domainURL;
    private String imgSrc;
    private String error;
    private SupplierService supplierService;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("supplierToModify") Supplier supplierToModify) {
        init();
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        supplierService = context.getBean(SupplierService.class);
        if (supplierToModify != null)
        supplier = supplierToModify;
        else supplier = new Supplier();
    }

    @Command
    public void saveSupplier() throws IOException {

        supplierService.saveSupplier(supplier);

        Executions.sendRedirect("/pages/company-info/suppliers/suppliers.zul");
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDomainURL() {
        return domainURL;
    }

    public void setDomainURL(String domainURL) {
        this.domainURL = domainURL;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
