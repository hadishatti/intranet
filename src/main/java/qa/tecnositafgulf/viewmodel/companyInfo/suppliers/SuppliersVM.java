package qa.tecnositafgulf.viewmodel.companyInfo.suppliers;

import net.sf.jasperreports.engine.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.sys.PageCtrl;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;
import qa.tecnositafgulf.config.MyProperties;
import qa.tecnositafgulf.model.reports.SupplierDatasource;
import qa.tecnositafgulf.model.suppliers.Supplier;
import qa.tecnositafgulf.searchcriteria.SupplierSearchCriteria;
import qa.tecnositafgulf.service.AuthenticationService;
import qa.tecnositafgulf.service.SupplierService;
import qa.tecnositafgulf.spring.config.AppConfig;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ledio on 5/6/18.
 */
public class SuppliersVM extends IntranetVM{

    private Supplier supplier;
    private String name;
    private String domainURL;
    private String imgSrc;
    private String error;
    private boolean visible;
    private Integer totalSize;
    private Integer activePage;

    private SupplierService supplierService;
    private List<Supplier> suppliers;
    private AuthenticationService authenticationService;
    private SupplierSearchCriteria supplierSearchCriteria;
    private String reportPath;
    private String leaveRequestReportTemplateName;

    @AfterCompose
    public void createAndPopulateHtml(@ContextParam(ContextType.VIEW) Component view){
        init();
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        supplierService = context.getBean(SupplierService.class);
        authenticationService = context.getBean(AuthenticationService.class);
        supplierSearchCriteria = new SupplierSearchCriteria();
        this.setActivePage(supplierSearchCriteria.getStartIndex());
        visible = false;
        readSuppliers();
        addCommonTags((PageCtrl) view.getPage());
        reportPath = MyProperties.getInstance().getResourcePath()+"/reports/";
        leaveRequestReportTemplateName = "Supplier_Report.jrxml";
    }

    @Command
    public void exportPDF() {
        try {
            HashMap map = new HashMap<>();
            JRDataSource dataSource = new SupplierDatasource();
            JasperPrint jasperPrint;
            URL reportTemplateURL = new URL(reportPath+leaveRequestReportTemplateName);
            InputStream reportTemplate = reportTemplateURL.openStream();
            JasperReport report = JasperCompileManager.compileReport(reportTemplate);
            jasperPrint = JasperFillManager.fillReport(report, map, dataSource);
            byte[] document = JasperExportManager.exportReportToPdf(jasperPrint);
            Filedownload.save(document, "application/pdf", "Suppliers_Report");
        }catch (Exception e){
            Messagebox.show("An error occurred! \n"+e.toString(), "Error", Messagebox.OK, Messagebox.ERROR);
            return;
        }

    }

    public void loadData(){
        this.setTotalSize(this.supplierService.geSupplierCount(supplierSearchCriteria));
        this.supplierSearchCriteria.setStartIndex(getActivePage());
        this.suppliers = this.supplierService.getSuppliers(supplierSearchCriteria);
    }

    @Command
    @NotifyChange({"activePage", "totalSize", "supplierSearchCriteria", "suppliers"})
    public void order(@BindingParam("orderBy") String orderBy){
        this.supplierSearchCriteria.setOrderByFieldName(orderBy);
        this.supplierSearchCriteria.setPageOrderMode("asc".equalsIgnoreCase(this.supplierSearchCriteria.getPageOrderMode()) ? "desc" : "asc");
        //load method call to load data
        loadData();
    }

    @Command
    @NotifyChange({"activePage", "totalSize", "supplierSearchCriteria", "suppliers"})
    public void filter(){
        this.supplierSearchCriteria.clear();
        this.supplierSearchCriteria.setPageOrderMode("desc");
        this.activePage = this.supplierSearchCriteria.getStartIndex();
        this.loadData();
    }

    @Command
    @NotifyChange({"activePage", "totalSize", "supplierSearchCriteria", "suppliers"})
    public void clearFilters(){
        this.supplierSearchCriteria.clear();
        this.supplierSearchCriteria.setPageOrderMode("desc");
        this.supplierSearchCriteria.clearFilters();
        this.loadData();
    }

    @Command
    public void modify(@BindingParam("item") Supplier supplier){
        final Map<String, Supplier> params = new HashMap<String, Supplier>();
        params.put("supplierToModify", supplier);
        ((Window) Executions.getCurrent().createComponents("/pages/company-info/suppliers/saveSupplier.zul", null, params)).doModal();
    }

    @Command
    public void delete(@BindingParam("item") final Supplier supplier){

        Messagebox.show("Do you want to delete this supplier?", "Warning", Messagebox.OK | Messagebox.CANCEL, Messagebox.INFORMATION, new EventListener<Event>() {
            public void onEvent(Event event) throws Exception {
                if (event.getName().equals("onOK")) {
                    supplierService.removeSupplier(supplier);
                    suppliers = supplierService.listSuppliers();
                    Executions.sendRedirect("/pages/company-info/suppliers/editSuppliers.zul");
                }
            }
        });

    }

    @Command
    public void saveSupplier(){
        final Map<String, Supplier> params = new HashMap<String, Supplier>();
        params.put("productToModify", null);
        ((Window) Executions.getCurrent().createComponents("/pages/company-info/suppliers/saveSupplier.zul",null, params)).doModal();
    }

    @Command
    public void viewSuppliers(){
        Executions.sendRedirect("/pages/company-info/suppliers/editSuppliers.zul");
    }

    @Command
    public void viewReport(){
        Executions.sendRedirect("/pages/company-info/suppliers/supplierReport.zul");
    }

    private void readSuppliers(){
        showButton();
        suppliers = supplierService.listSuppliers();
        String html = "";

        if (!suppliers.isEmpty() && suppliers != null)
        for(Supplier s : suppliers){
            html += "<div class=\"col-lg-3 col-md-4 col-xs-6\">" +
                    "<a href=\""+s.getDomainURL()+"\" class=\"d-block mb-4\">" +
                    " <img class=\"img-fluid img-thumbnail\" src=\""+ MyProperties.getInstance().getImagePath() + s.getImgSrc()+"\" alt=\"\">" +
                    "</a>" +
                    "</div>";
        }

        String script = "";

        if (!html.isEmpty()) {
            script += "document.getElementById(\"suppliers\").innerHTML = '" + html + "';";
        }
        Clients.evalJavaScript(script);
    }

    private void showButton(){
        if (authenticationService.isAdministrator()) {
            visible = true;
        }
    }

    @NotifyChange({"activePage", "totalSize", "supplierSearchCriteria", "suppliers"})
    public void setActivePage(Integer activePage) {
        this.activePage = activePage;
        loadData();
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

    public List<Supplier> getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(List<Supplier> suppliers) {
        this.suppliers = suppliers;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
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

    public SupplierSearchCriteria getSupplierSearchCriteria() {
        return supplierSearchCriteria;
    }

    public void setSupplierSearchCriteria(SupplierSearchCriteria supplierSearchCriteria) {
        this.supplierSearchCriteria = supplierSearchCriteria;
    }
}
