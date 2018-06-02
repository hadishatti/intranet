package qa.tecnositafgulf.viewmodel.companyInfo.partners;

import net.sf.jasperreports.engine.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.sys.PageCtrl;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;
import qa.tecnositafgulf.config.MyProperties;
import qa.tecnositafgulf.model.companyInfo.Partner;
import qa.tecnositafgulf.model.reports.PartnerReportDataSource;
import qa.tecnositafgulf.searchcriteria.PartnerSearchCriteria;
import qa.tecnositafgulf.service.CompanyInfoService;
import qa.tecnositafgulf.spring.config.AppConfig;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ameljo on 05/05/18.
 */
public class ViewPartnersVM extends IntranetVM {

    private List<Partner> partners;
    private CompanyInfoService service;
    private String htmlContent;
    private Integer activePage;
    private Integer totalSize;
    private PartnerSearchCriteria searchCriteria;
    private String resource = MyProperties.getInstance().getResourcePath();
    private String reportPath;
    private String leaveRequestReportTemplateName;

    @Init
    public void init(){
        super.init();
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        service = context.getBean(CompanyInfoService.class);
        searchCriteria = new PartnerSearchCriteria();
        this.setActivePage(this.searchCriteria.getStartIndex());
        loadData();
        htmlContent = prepareHtml();
        reportPath = MyProperties.getInstance().getResourcePath()+"/reports/";
        leaveRequestReportTemplateName = "partner-report.jrxml";

    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        init();
        addCommonTags((PageCtrl) view.getPage());
    }

    private String prepareHtml(){
        String ret = "<head>" +
                "<meta charset=\"utf-8\">" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">" +
                "<meta name=\"description\" content=\"\">" +
                "<meta name=\"author\" content=\"\">" +
                "<title>Intranet</title>" +
                "<!-- Bootstrap core CSS -->" +
                "<link href=\"" + resource + "/bootstrap/css/bootstrap.min.css\" rel=\"stylesheet\">" +
                "<!-- Custom styles for this template -->" +
                "<link href=\"" + resource + "/customized/css/partners_thumbnail_gallery.css\" rel=\"stylesheet\">" +
                "</head>" +
                "<body>" +
                "<div class=\"container\">" +
                "<h1 class=\"my-4 text-center text-lg-left\" style=\"color:#1a4280\">Our Partners</h1>" +
                "<div id=\"divPartner\" class=\"row text-center text-lg-left\">";
        if(partners != null && !partners.isEmpty()){
            for (Partner partner: partners){
                String temp = "<div class=\"col-lg-3 col-md-4 col-xs-6\">" +
                        "    <a href=\"" + partner.getHref() + "\" class=\"d-block mb-4 h-100\">" +
                        "    <img class=\"img-fluid img-thumbnail\" src=\""+ MyProperties.getInstance().getImagePath() + partner.getImg() +"\" alt=\"\">" +
                        "    </a>" +
                        "    </div>";
                ret += temp;
            }
        }

        ret += "</div></div>";

        return ret;
    }

    public void loadData(){
        this.setTotalSize(this.service.getPartnersCount(searchCriteria));
        this.searchCriteria.setStartIndex(getActivePage());
        this.partners = this.service.getPartners(searchCriteria);
    }

    @Command
    public void exportPDF() {
        try {
            HashMap map = new HashMap<>();
            JRDataSource dataSource = new PartnerReportDataSource();
            JasperPrint jasperPrint;
            URL reportTemplateURL = new URL(reportPath+leaveRequestReportTemplateName);
            InputStream reportTemplate = reportTemplateURL.openStream();
            JasperReport report = JasperCompileManager.compileReport(reportTemplate);
            jasperPrint = JasperFillManager.fillReport(report, map, dataSource);
            byte[] document = JasperExportManager.exportReportToPdf(jasperPrint);
            Filedownload.save(document, "application/pdf", "Partners_Report");
        }catch (Exception e){
            Messagebox.show("An error occurred! \n"+e.toString(), "Error", Messagebox.OK, Messagebox.ERROR);
            return;
        }

    }

    @Command
    @NotifyChange({"activePage", "totalSize", "partnerSearchCriteria", "partners"})
    public void order(@BindingParam("orderBy") String orderBy){
        this.searchCriteria.setOrderByFieldName(orderBy);
        this.searchCriteria.setPageOrderMode("asc".equalsIgnoreCase(this.searchCriteria.getPageOrderMode()) ? "desc" : "asc");
        //load method call to load data
        loadData();
    }

    @Command
    @NotifyChange({"activePage", "totalSize", "partnerSearchCriteria", "partners"})
    public void clearFilters() {
        this.searchCriteria.clear();
        this.searchCriteria.setPageOrderMode("desc");
        this.searchCriteria.clearFilters();
        this.loadData();
    }

    @Command
    @NotifyChange({"activePage", "totalSize", "partnerSearchCriteria", "partners"})
    public void filter() {
        this.searchCriteria.clear();
        this.searchCriteria.setPageOrderMode("desc");
        this.activePage = this.searchCriteria.getStartIndex();
        this.loadData();
    }

    @NotifyChange({"activePage", "totalSize", "partnerSearchCriteria", "partners"})
    public void setActivePage(Integer activePage) {
        this.activePage = activePage;
        loadData();
    }

    @Command
    @NotifyChange("partners")
    public void add(){
        final Map<String, Partner> params = new HashMap<String, Partner>();
        params.put("partnerToModify", null);
        ((Window) Executions.getCurrent().createComponents("/pages/company-info/partners/savePartner.zul", null, params)).doModal();
    }

    @Command
    @NotifyChange("partners")
    public void modify(@BindingParam("item") Partner partner){
        final Map<String, Partner> params = new HashMap<String, Partner>();
        params.put("partnerToModify", partner);
        ((Window) Executions.getCurrent().createComponents("/pages/company-info/partners/savePartner.zul", null, params)).doModal();
    }

    @Command
    @NotifyChange("partners")
    public void delete(@BindingParam("item") final Partner partner){
        Messagebox.show("Do you want to delete this partner?", "Warning", Messagebox.OK | Messagebox.CANCEL, Messagebox.INFORMATION, new EventListener<Event>() {
            public void onEvent(Event event) throws Exception {
                if (event.getName().equals("onOK")) {
                    partners.remove(partner);
                    service.remove(partner);
                    Executions.sendRedirect("/pages/company-info/partners/modifyPartner.zul");
                }
            }
        });

    }

    public List<Partner> getPartners(){
        return this.partners;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public void setPartners(List<Partner> partners) {
        this.partners = partners;
    }

    public Integer getActivePage() {
        return activePage;
    }

    public Integer getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Integer totalSize) {
        this.totalSize = totalSize;
    }

    public PartnerSearchCriteria getSearchCriteria() {
        return searchCriteria;
    }

    public void setSearchCriteria(PartnerSearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;
    }
}
